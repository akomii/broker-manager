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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.utils.FilesystemIdGenerator;
import org.aktin.broker.query.xml.QuerySchedule;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public class FilesystemManagerRequestRepository implements ManagerRequestRepository {

  private static final String JSON_EXTENSION = ".json";
  private static final String FILE_SEPARATOR = File.separator;

  private final ObjectMapper mapper;
  private final String storageDirectory;

  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();
  private final FilesystemIdGenerator filesystemIdGenerator;

  public FilesystemManagerRequestRepository(ObjectMapper mapper, String storageDirectory) throws IOException {
    this.mapper = mapper;
    this.storageDirectory = storageDirectory;
    Path storagePath = Paths.get(storageDirectory);
    Files.createDirectories(storagePath);
    filesystemIdGenerator = new FilesystemIdGenerator(storagePath);
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @CacheEvict(cacheNames = "managerRequests", key = "#entity.id")
  @Override
  public int save(ManagerRequest<QuerySchedule> entity) throws DataPersistException {
    if (entity.getId() == 0) {
      entity.setId(filesystemIdGenerator.generateId());
    }
    String filename = storageDirectory + FILE_SEPARATOR + entity.getId() + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.writeLock().lock();
    try {
      mapper.writeValue(new File(filename), entity);
      return entity.getId();
    } catch (IOException e) {
      throw new DataPersistException("Failed to save ManagerRequest: " + entity.getId(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @CacheEvict(cacheNames = "managerRequests", key = "#id")
  @Override
  public void delete(int id) throws DataDeleteException {
    String filename = storageDirectory + FILE_SEPARATOR + id + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.writeLock().lock();
    try {
      Files.deleteIfExists(Paths.get(filename));
    } catch (IOException e) {
      throw new DataDeleteException("Failed to delete ManagerRequest: " + id, e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerRequests", key = "#id")
  @Override
  public Optional<ManagerRequest<QuerySchedule>> get(int id) throws DataReadException {
    String filename = storageDirectory + FILE_SEPARATOR + id + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.readLock().lock();
    try {
      File file = new File(filename);
      return file.exists() ? deserializeManagerRequest(file) : Optional.empty();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerRequests")
  @Override
  public List<ManagerRequest<QuerySchedule>> getAll() throws DataReadException {
    List<ManagerRequest<QuerySchedule>> managerRequests = new ArrayList<>();
    File storageDir = new File(storageDirectory);
    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(JSON_EXTENSION));
    if (files == null) {
      return managerRequests;
    }
    for (File file : files) {
      String filename = file.getAbsolutePath();
      ReentrantReadWriteLock lock = getLock(filename);
      lock.readLock().lock();
      try {
        Optional<ManagerRequest<QuerySchedule>> managerRequest = deserializeManagerRequest(file);
        managerRequest.ifPresent(managerRequests::add);
      } finally {
        lock.readLock().unlock();
      }
    }
    return managerRequests;
  }

  private Optional<ManagerRequest<QuerySchedule>> deserializeManagerRequest(File file) {
    try {
      return Optional.of(mapper.readValue(file, ManagerRequest.class));
    } catch (IOException e) {
      throw new DataReadException("Error reading ManagerRequest from file: " + file.getName(), e);
    }
  }
}
