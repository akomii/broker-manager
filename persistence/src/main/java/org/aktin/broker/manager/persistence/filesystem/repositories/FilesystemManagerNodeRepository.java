/*
 * Copyright (c) 2024 AKTIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.persistence.filesystem.repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.xml.bind.JAXBException;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlMarshaller;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlUnmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

// ManagerNodes are registered on the broker-server, broker-manager only mirrors them, so no id generation is necessary
public class FilesystemManagerNodeRepository implements ManagerNodeRepository {

  private static final Logger log = LoggerFactory.getLogger(FilesystemManagerNodeRepository.class);
  private static final String XML_EXTENSION = ".xml";

  private final XmlMarshaller xmlMarshaller;
  private final XmlUnmarshaller<ManagerNode> xmlUnmarshaller;
  private final String storageDirectory;
  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FilesystemManagerNodeRepository(XmlMarshaller xmlMarshaller, XmlUnmarshaller<ManagerNode> xmlUnmarshaller, String storageDirectory)
      throws IOException {
    this.xmlMarshaller = xmlMarshaller;
    this.xmlUnmarshaller = xmlUnmarshaller;
    this.storageDirectory = storageDirectory;
    Files.createDirectories(Paths.get(this.storageDirectory));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#entity.id")
  @Override
  public int save(ManagerNode entity) throws DataPersistException {
    String filename = Paths.get(storageDirectory, entity.getId() + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filename);
    lock.writeLock().lock();
    try {
      File file = new File(filename);
      xmlMarshaller.marshal(entity, file);
      return entity.getId();
    } catch (JAXBException e) {
      throw new DataPersistException("Failed to save ManagerNode: " + entity.getId(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#id")
  @Override
  public void delete(int id) throws DataDeleteException {
    String filename = Paths.get(storageDirectory, id + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filename);
    lock.writeLock().lock();
    try {
      boolean deleted = Files.deleteIfExists(Paths.get(filename));
      if (!deleted) {
        log.warn("ManagerNode file not found for deletion: {}", filename);
      } else {
        log.info("Deleted ManagerNode file: {}", filename);
      }
    } catch (IOException e) {
      throw new DataDeleteException("Error deleting ManagerNode: " + filename, e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes", key = "#id")
  @Override
  public Optional<ManagerNode> get(int id) throws DataReadException {
    String filename = Paths.get(storageDirectory, id + XML_EXTENSION).toString();
    File file = new File(filename);
    if (!file.exists()) {
      return Optional.empty();
    }
    ReentrantReadWriteLock lock = getLock(filename);
    lock.readLock().lock();
    try {
      return Optional.of(xmlUnmarshaller.unmarshal(file));
    } catch (JAXBException | DataMigrationException | IOException e) {
      throw new DataReadException("Error retrieving ManagerNode: " + filename, e);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes")
  @Override
  public List<ManagerNode> getAll() {
    List<ManagerNode> managerNodes = new ArrayList<>();
    File storageDir = new File(storageDirectory);
    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(XML_EXTENSION));
    if (files == null) {
      return managerNodes;
    }
    for (File file : files) {
      String filename = file.getAbsolutePath();
      ReentrantReadWriteLock lock = getLock(filename);
      lock.readLock().lock();
      try {
        ManagerNode node = xmlUnmarshaller.unmarshal(file);
        if (node != null) {
          managerNodes.add(node);
        }
      } catch (JAXBException | DataMigrationException | IOException e) {
        log.warn("Error retrieving ManagerNode: {}, skipping...", filename, e);
      } finally {
        lock.readLock().unlock();
      }
    }
    return managerNodes;
  }
}
