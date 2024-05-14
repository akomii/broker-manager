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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.persistence.api.exceptions.DeletePersistedDataException;
import org.aktin.broker.manager.persistence.api.exceptions.PersistDataException;
import org.aktin.broker.manager.persistence.api.exceptions.ReadPersistedDataException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerNodeRepositoryImpl implements ManagerNodeRepository {

  private static final String JSON_EXTENSION = ".json";
  private static final String FILE_SEPARATOR = File.separator;

  @Value("${broker-manager.storage.directory.nodes}")
  private String storageDirectory;

  private final ObjectMapper mapper;

  private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Lock readLock = rwLock.readLock();
  private final Lock writeLock = rwLock.writeLock();

  public ManagerNodeRepositoryImpl(@Autowired ObjectMapper mapper) throws IOException {
    this.mapper = mapper;
    Files.createDirectories(Paths.get(this.storageDirectory));
  }

  public ManagerNodeRepositoryImpl(ObjectMapper mapper, String storageDirectory) throws IOException {
    this.mapper = mapper;
    this.storageDirectory = storageDirectory;
    Files.createDirectories(Paths.get(this.storageDirectory));
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#entity.id")
  @Override
  public void save(ManagerNode entity) throws PersistDataException {
    writeLock.lock();
    try {
      String filename = storageDirectory + FILE_SEPARATOR + entity.getId() + JSON_EXTENSION;
      mapper.writeValue(new File(filename), entity);
    } catch (IOException e) {
      throw new PersistDataException("Failed to save ManagerNode: " + entity.getId(), e);
    } finally {
      writeLock.unlock();
    }
  }

  @CacheEvict(cacheNames = "managerNodes", key = "#id")
  @Override
  public void delete(int id) throws DeletePersistedDataException {
    writeLock.lock();
    try {
      String filename = storageDirectory + FILE_SEPARATOR + id + JSON_EXTENSION;
      Files.deleteIfExists(Paths.get(filename));
    } catch (IOException e) {
      throw new DeletePersistedDataException("Failed to delete ManagerNode: " + id, e);
    } finally {
      writeLock.unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes", key = "#id")
  @Override
  public Optional<ManagerNode> get(int id) throws ReadPersistedDataException {
    readLock.lock();
    try {
      String filename = storageDirectory + FILE_SEPARATOR + id + JSON_EXTENSION;
      File file = new File(filename);
      return file.exists() ? deserializeManagerNode(file) : Optional.empty();
    } finally {
      readLock.unlock();
    }
  }

  @Cacheable(cacheNames = "managerNodes")
  @Override
  public List<ManagerNode> getAll() throws ReadPersistedDataException {
    readLock.lock();
    try {
      File storageDir = new File(storageDirectory);
      File[] files = storageDir.listFiles((dir, name) -> name.endsWith(JSON_EXTENSION));
      if (files == null) {
        return List.of();
      }
      return Arrays.stream(files)
          .map(this::deserializeManagerNode)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .toList();
    } finally {
      readLock.unlock();
    }
  }

  private Optional<ManagerNode> deserializeManagerNode(File file) {
    try {
      return Optional.of(mapper.readValue(file, ManagerNode.class));
    } catch (IOException e) {
      throw new ReadPersistedDataException("Error reading ManagerNode from file: " + file.getName(), e);
    }
  }
}
