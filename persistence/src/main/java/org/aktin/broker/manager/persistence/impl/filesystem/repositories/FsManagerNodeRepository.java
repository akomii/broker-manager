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

package org.aktin.broker.manager.persistence.impl.filesystem.repositories;

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
import org.aktin.broker.manager.model.models.ManagerNode;
import org.aktin.broker.manager.persistence.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.aktin.broker.manager.persistence.repositories.ManagerNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

// ManagerNodes are registered on the broker-server, broker-manager only mirrors them, so no id generation is necessary
//TODO refactor
public class FsManagerNodeRepository implements ManagerNodeRepository {

  private static final Logger log = LoggerFactory.getLogger(FsManagerNodeRepository.class);
  private static final String XML_EXTENSION = ".xml";

  private final XmlMarshaller xmlMarshaller;
  private final XmlUnmarshaller<ManagerNode> xmlUnmarshaller;
  private final String nodesDirectory;

  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FsManagerNodeRepository(XmlMarshaller xmlMarshaller, XmlUnmarshaller<ManagerNode> xmlUnmarshaller, String nodesDirectory)
      throws IOException {
    this.xmlMarshaller = xmlMarshaller;
    this.xmlUnmarshaller = xmlUnmarshaller;
    this.nodesDirectory = nodesDirectory;
    Files.createDirectories(Paths.get(this.nodesDirectory));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#entity.id")
  @Override
  public int save(ManagerNode entity) throws DataPersistException {
    String filePath = Paths.get(nodesDirectory, entity.getId() + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        log.info("Creating new ManagerNode: {}", filePath);
      }
      xmlMarshaller.marshal(entity, file);
      return entity.getId();
    } catch (Exception e) {
      throw new DataPersistException("Failed to save ManagerNode: " + entity.getId(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#id")
  @Override
  public void delete(int id) throws DataDeleteException {
    String filePath = Paths.get(nodesDirectory, id + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      boolean deleted = Files.deleteIfExists(Paths.get(filePath));
      if (!deleted) {
        log.warn("ManagerNode file not found for deletion: {}", filePath);
      } else {
        log.info("Deleted ManagerNode file: {}", filePath);
      }
    } catch (Exception e) {
      throw new DataDeleteException("Error deleting ManagerNode: " + filePath, e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes", key = "#id")
  @Override
  public Optional<ManagerNode> get(int id) throws DataReadException {
    String filePath = Paths.get(nodesDirectory, id + XML_EXTENSION).toString();
    File file = new File(filePath);
    if (!file.exists()) {
      return Optional.empty();
    }
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.readLock().lock();
    try {
      return Optional.of(xmlUnmarshaller.unmarshal(file));
    } catch (Exception e) {
      throw new DataReadException("Error retrieving ManagerNode: " + filePath, e);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes")
  @Override
  public List<ManagerNode> getAll() {
    List<ManagerNode> managerNodes = new ArrayList<>();
    File storageDir = new File(nodesDirectory);
    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(XML_EXTENSION));
    if (files == null) {
      return managerNodes;
    }
    for (File file : files) {
      String filePath = file.getAbsolutePath();
      ReentrantReadWriteLock lock = getLock(filePath);
      lock.readLock().lock();
      try {
        ManagerNode node = xmlUnmarshaller.unmarshal(file);
        if (node != null) {
          managerNodes.add(node);
        }
      } catch (Exception e) {
        log.warn("Error retrieving ManagerNode: {}, skipping...", filePath, e);
      } finally {
        lock.readLock().unlock();
      }
    }
    return managerNodes;
  }
}
