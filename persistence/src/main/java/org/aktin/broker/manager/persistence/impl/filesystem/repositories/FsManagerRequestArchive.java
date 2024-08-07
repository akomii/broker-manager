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
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.exceptions.DataArchiveException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestArchive;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

//TODO refactor and simplify
public class FsManagerRequestArchive implements ManagerRequestArchive {

  private static final Logger log = LoggerFactory.getLogger(FsManagerRequestArchive.class);
  private static final String XML_EXTENSION = ".xml";

  private final XmlMarshaller xmlMarshaller;
  private final XmlUnmarshaller<ManagerRequest> xmlUnmarshaller;
  private final String archiveDirectory;

  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FsManagerRequestArchive(XmlMarshaller xmlMarshaller, XmlUnmarshaller<ManagerRequest> xmlUnmarshaller, String archiveDirectory)
      throws IOException {
    this.xmlMarshaller = xmlMarshaller;
    this.xmlUnmarshaller = xmlUnmarshaller;
    this.archiveDirectory = archiveDirectory;
    Files.createDirectories(Path.of(this.archiveDirectory));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @Override
  public int save(ManagerRequest entity) throws DataArchiveException {
    String filePath = Path.of(archiveDirectory, entity.getId() + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      log.info("Archiving ManagerRequest: {}", filePath);
      File file = new File(filePath);
      xmlMarshaller.marshal(entity, file);
      return entity.getId();
    } catch (Exception e) {
      throw new DataPersistException("Failed to save ManagerRequest: " + entity.getId(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "requestsArchive", key = "#id")
  @Override
  public Optional<ManagerRequest> get(int id) throws DataReadException {
    String filePath = Path.of(archiveDirectory, id + XML_EXTENSION).toString();
    File file = new File(filePath);
    if (!file.exists()) {
      return Optional.empty();
    }
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.readLock().lock();
    try {
      return Optional.of(xmlUnmarshaller.unmarshal(file));
    } catch (Exception e) {
      throw new DataReadException("Error retrieving archived ManagerRequest: " + filePath, e);
    } finally {
      lock.readLock().unlock();
    }
  }
}
