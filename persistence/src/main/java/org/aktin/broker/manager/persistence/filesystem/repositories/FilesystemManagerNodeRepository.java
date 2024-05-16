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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.persistence.api.exceptions.DeletePersistedDataException;
import org.aktin.broker.manager.persistence.api.exceptions.PersistDataException;
import org.aktin.broker.manager.persistence.api.exceptions.ReadPersistedDataException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public class FilesystemManagerNodeRepository implements ManagerNodeRepository {

  private static final String JSON_EXTENSION = ".json";
  private static final String FILE_SEPARATOR = File.separator;

  private final ObjectMapper mapper;
  private final String storageDirectory;

  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FilesystemManagerNodeRepository(ObjectMapper mapper, String storageDirectory) throws IOException {
    this.mapper = mapper;
    this.storageDirectory = storageDirectory;
    Files.createDirectories(Paths.get(this.storageDirectory));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#entity.id")
  @Override
  public void save(ManagerNode entity) throws PersistDataException {
    String filename = storageDirectory + FILE_SEPARATOR + entity.getId() + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.writeLock().lock();
    try {
      mapper.writeValue(new File(filename), entity);
    } catch (IOException e) {
      throw new PersistDataException("Failed to save ManagerNode: " + entity.getId(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#id")
  @Override
  public void delete(int id) throws DeletePersistedDataException {
    String filename = storageDirectory + FILE_SEPARATOR + id + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.writeLock().lock();
    try {
      Files.deleteIfExists(Paths.get(filename));
    } catch (IOException e) {
      throw new DeletePersistedDataException("Failed to delete ManagerNode: " + id, e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes", key = "#id")
  @Override
  public Optional<ManagerNode> get(int id) throws ReadPersistedDataException {
    String filename = storageDirectory + FILE_SEPARATOR + id + JSON_EXTENSION;
    ReentrantReadWriteLock lock = getLock(filename);
    lock.readLock().lock();
    try {
      File file = new File(filename);
      return file.exists() ? deserializeManagerNode(file) : Optional.empty();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes")
  @Override
  public List<ManagerNode> getAll() throws ReadPersistedDataException {
    List<ManagerNode> managerNodes = new ArrayList<>();
    File storageDir = new File(storageDirectory);
    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(JSON_EXTENSION));
    if (files == null) {
      return managerNodes;
    }
    for (File file : files) {
      String filename = file.getAbsolutePath();
      ReentrantReadWriteLock lock = getLock(filename);
      lock.readLock().lock();
      try {
        Optional<ManagerNode> managerNode = deserializeManagerNode(file);
        managerNode.ifPresent(managerNodes::add);
      } finally {
        lock.readLock().unlock();
      }
    }
    return managerNodes;
  }

  private Optional<ManagerNode> deserializeManagerNode(File file) {
    try {
      return Optional.of(mapper.readValue(file, ManagerNode.class));
    } catch (IOException e) {
      throw new ReadPersistedDataException("Error reading ManagerNode from file: " + file.getName(), e);
    }
  }
}
