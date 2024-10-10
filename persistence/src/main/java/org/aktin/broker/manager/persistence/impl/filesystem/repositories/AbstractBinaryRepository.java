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
import java.util.ArrayList;
import java.util.List;
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

  protected void saveFile(InputStream data, String filename) throws DataPersistException {
    Path filePath = Paths.get(directory, filename);
    Path tmpFilePath = Paths.get(directory, filename + TMP_EXTENSION);
    try {
      // Write data to temporary file
      try (OutputStream outputStream = Files.newOutputStream(tmpFilePath)) {
        data.transferTo(outputStream);
      }
      moveTempFile(filePath, tmpFilePath);
    } catch (Exception e) {
      throw new DataPersistException("Failed to save data to file: " + filePath, e);
    }
  }

  protected void moveTempFile(Path filePath, Path tmpFilePath) throws IOException {
    try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        FileLock lock = channel.lock()) {
      boolean isNewFile = !Files.exists(filePath);
      Files.move(tmpFilePath, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      if (isNewFile) {
        log.info("Created new file: {}", filePath);
      } else {
        log.info("Updated file: {}", filePath);
      }
    } finally {
      try {
        Files.deleteIfExists(tmpFilePath);
      } catch (IOException ex) {
        log.warn("Failed to delete temporary file: {}", tmpFilePath, ex);
      }
    }
  }

  protected Optional<InputStream> getFile(String filename) throws DataReadException {
    Path filePath = Paths.get(directory, filename);
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

  protected List<InputStream> getAllFiles() throws DataReadException {
    List<InputStream> inputStreams = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory))) {
      for (Path filePath : stream) {
        // Skip temporary files
        if (filePath.toString().endsWith(TMP_EXTENSION)) {
          continue;
        }
        if (Files.isRegularFile(filePath)) {
          try {
            InputStream inputStream = Files.newInputStream(filePath);
            inputStreams.add(inputStream);
          } catch (Exception e) {
            log.warn("Error retrieving data from file: {}, skipping...", filePath, e);
          }
        }
      }
    } catch (IOException e) {
      throw new DataReadException("Error reading files from directory: " + directory, e);
    }
    return inputStreams;
  }

  protected void deleteFile(String filename) throws DataDeleteException {
    Path filePath = Paths.get(directory, filename);
    try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE); FileLock lock = channel.lock()) {
      boolean deleted = Files.deleteIfExists(filePath);
      if (deleted) {
        log.info("File deleted: {}", filePath);
      } else {
        log.warn("File not found for deletion: {}", filePath);
      }
    } catch (NoSuchFileException e) {
      log.warn("File not found for deletion: {}", filePath);
    } catch (Exception e) {
      throw new DataDeleteException("Error deleting file: " + filePath, e);
    }
  }
}
