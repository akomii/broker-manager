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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// each stored result is unique
// TODO refactor
public class FsExecutionResultRepository implements ExecutionResultRepository {

  private static final Logger log = LoggerFactory.getLogger(FsExecutionResultRepository.class);

  private final String resultsDirectory;
  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FsExecutionResultRepository(String resultsDirectory) throws IOException {
    this.resultsDirectory = resultsDirectory;
    Files.createDirectories(Path.of(resultsDirectory));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @Override
  public void save(InputStream result, String identifier) throws DataPersistException {
    String filePath = Path.of(resultsDirectory, identifier).toString();
    File file = new File(filePath);
    if (file.exists()) {
      throw new DataPersistException("Execution result file must be unique. File already exists: " + filePath);
    }
    try (OutputStream outputStream = Files.newOutputStream(Path.of(filePath))) {
      result.transferTo(outputStream);
      log.info("Saved new result to: {}", filePath);
    } catch (Exception e) {
      throw new DataPersistException("Failed to save execution results to file: " + filePath, e);
    }
  }

  @Override
  public Optional<InputStream> get(String identifier) throws DataReadException {
    String filePath = Path.of(resultsDirectory, identifier).toString();
    File file = new File(filePath);
    if (!file.exists()) {
      return Optional.empty();
    }
    ReentrantReadWriteLock lock = getLock(identifier);
    lock.readLock().lock();
    try {
      return Optional.of(Files.newInputStream(Path.of(filePath)));
    } catch (Exception e) {
      throw new DataReadException("Error retrieving execution result: " + filePath, e);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void delete(String identifier) throws DataDeleteException {
    String filePath = Path.of(resultsDirectory, identifier).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      boolean deleted = Files.deleteIfExists(Path.of(filePath));
      if (!deleted) {
        log.warn("Execution result not found for deletion: {}", filePath);
      } else {
        log.info("Deleted execution result: {}", filePath);
      }
    } catch (Exception e) {
      throw new DataDeleteException("Error deleting execution result: " + filePath, e);
    } finally {
      lock.writeLock().unlock();
    }
  }
}
