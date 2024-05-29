package org.aktin.broker.manager.persistence.filesystem.repositories;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// each stored result is unique
// TODO refactor
public class FsExecutionResultsRepository implements ExecutionResultsRepository {

  private static final Logger log = LoggerFactory.getLogger(FsExecutionResultsRepository.class);

  private final String resultsDirectory;
  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

  public FsExecutionResultsRepository(String resultsDirectory) throws IOException {
    this.resultsDirectory = resultsDirectory;
    Files.createDirectories(Paths.get(resultsDirectory));
  }

  private ReentrantReadWriteLock getLock(String filename) {
    return fileLocks.computeIfAbsent(filename, f -> new ReentrantReadWriteLock());
  }

  @Override
  public String save(InputStream result, String filename) throws DataPersistException {
    String filePath = Paths.get(resultsDirectory, filename).toString();
    File file = new File(filePath);
    if (file.exists()) {
      throw new DataPersistException("Execution result file must be unique. File already exists: " + filePath);
    }
    try (OutputStream outputStream = Files.newOutputStream(Path.of(filePath))) {
      result.transferTo(outputStream);
      log.info("Saved result to: {}", filePath);
      return filename;
    } catch (Exception e) {
      throw new DataPersistException("Failed to save execution results to file: " + filePath, e);
    }
  }

  @Override
  public Optional<InputStream> get(String filename) throws DataReadException {
    String filePath = Paths.get(resultsDirectory, filename).toString();
    File file = new File(filePath);
    if (!file.exists()) {
      return Optional.empty();
    }
    ReentrantReadWriteLock lock = getLock(filename);
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
  public void delete(String filename) throws DataDeleteException {
    String filePath = Paths.get(resultsDirectory, filename).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      boolean deleted = Files.deleteIfExists(Paths.get(filePath));
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
