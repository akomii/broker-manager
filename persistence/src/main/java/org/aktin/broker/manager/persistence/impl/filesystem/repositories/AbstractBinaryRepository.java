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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractBinaryRepository {

  private static final Logger log = LoggerFactory.getLogger(AbstractBinaryRepository.class);
  protected static final String TMP_EXTENSION = ".tmp";

  protected final String directory;

  protected AbstractBinaryRepository(String directory) throws IOException {
    this.directory = directory;
    Files.createDirectories(Paths.get(directory));
    cleanupTemporaryFiles();
  }

  private void cleanupTemporaryFiles() {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory), "*" + TMP_EXTENSION)) {
      for (Path entry : stream) {
        Files.deleteIfExists(entry);
        log.info("Deleted leftover temporary file: {}", entry.getFileName());
      }
    } catch (IOException e) {
      log.warn("Failed to clean up temporary files in directory {}: {}", directory, e.getMessage());
    }
  }

  protected void saveData(InputStream data, String identifier) throws DataPersistException {
    Path filePath = Paths.get(directory, identifier);
    Path tmpFilePath = Paths.get(directory, identifier + TMP_EXTENSION);
    try {
      // Write data to temporary file
      try (OutputStream outputStream = Files.newOutputStream(tmpFilePath)) {
        data.transferTo(outputStream);
      }
      // Acquire lock and move the temporary file to the target location
      try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE); FileLock lock = channel.lock()) {
        boolean isNewFile = !Files.exists(filePath);
        Files.move(tmpFilePath, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        if (isNewFile) {
          log.info("Created new data: {}", filePath);
        } else {
          log.info("Updated data: {}", filePath);
        }
      }
    } catch (Exception e) {
      throw new DataPersistException("Failed to save data to file: " + filePath, e);
    } finally {
      try {
        // Ensure temporary file is deleted
        Files.deleteIfExists(tmpFilePath);
      } catch (IOException ex) {
        log.warn("Failed to delete temporary file: {}", tmpFilePath, ex);
      }
    }
  }

  protected Optional<InputStream> getData(String identifier) throws DataReadException {
    Path filePath = Paths.get(directory, identifier);
    if (!Files.exists(filePath)) {
      return Optional.empty();
    }
    try {
      InputStream inputStream = Files.newInputStream(filePath);
      return Optional.of(inputStream);
    } catch (Exception e) {
      throw new DataReadException("Error retrieving data from file: " + filePath, e);
    }
  }

  protected void deleteData(String identifier) throws DataDeleteException {
    Path filePath = Paths.get(directory, identifier);
    try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE); FileLock lock = channel.lock()) {
      boolean deleted = Files.deleteIfExists(filePath);
      if (deleted) {
        log.info("Deleted data: {}", filePath);
      } else {
        log.warn("Data not found for deletion: {}", filePath);
      }
    } catch (NoSuchFileException e) {
      log.warn("Data not found for deletion: {}", filePath);
    } catch (Exception e) {
      throw new DataDeleteException("Error deleting data: " + filePath, e);
    }
  }
}
