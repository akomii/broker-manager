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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.persistence.api.exceptions.ArchiveDataException;
import org.aktin.broker.manager.persistence.api.exceptions.PersistDataException;
import org.aktin.broker.manager.persistence.api.exceptions.ReadPersistedDataException;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestArchive;
import org.aktin.broker.query.xml.QuerySchedule;
import org.springframework.cache.annotation.Cacheable;

public class FilesystemManagerRequestArchive implements ManagerRequestArchive {

  private static final String JSON_EXTENSION = ".json";
  private static final String FILE_SEPARATOR = File.separator;

  private final ObjectMapper mapper;
  private final String requestStorage;
  private final String archiveStorage;

  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FilesystemManagerRequestArchive(ObjectMapper mapper, String requestStorage, String archiveStorage) throws IOException {
    this.mapper = mapper;
    this.requestStorage = requestStorage;
    this.archiveStorage = archiveStorage;
    Files.createDirectories(Paths.get(this.archiveStorage));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @Override
  public void archive(int id) throws PersistDataException {
    String sourceFile = requestStorage + FILE_SEPARATOR + id + JSON_EXTENSION;
    String destinationFile = archiveStorage + FILE_SEPARATOR + id + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(sourceFile);
    lock.writeLock().lock();
    try {
      Files.move(Paths.get(sourceFile), Paths.get(destinationFile));
    } catch (IOException e) {
      throw new ArchiveDataException("Failed to archive ManagerRequest with ID: " + id, e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "requestsArchive", key = "#id")
  @Override
  public Optional<ManagerRequest<QuerySchedule>> get(int id) throws ReadPersistedDataException {
    String filename = archiveStorage + FILE_SEPARATOR + id + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.readLock().lock();
    try {
      File file = new File(filename);
      return file.exists() ? deserializeManagerRequest(file) : Optional.empty();
    } finally {
      lock.readLock().unlock();
    }
  }

  private Optional<ManagerRequest<QuerySchedule>> deserializeManagerRequest(File file) {
    try {
      return Optional.of(mapper.readValue(file, ManagerRequest.class));
    } catch (IOException e) {
      throw new ReadPersistedDataException("Error reading ManagerRequest from file: " + file.getName(), e);
    }
  }
}
