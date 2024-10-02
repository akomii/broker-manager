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
import org.aktin.broker.manager.model.api.models.PersistentObject;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractXMLRepository<T extends PersistentObject> {

  private static final Logger log = LoggerFactory.getLogger(AbstractXMLRepository.class);
  private static final String FILE_EXTENSION = ".xml";
  private static final String TMP_EXTENSION = ".tmp";

  protected final XmlMarshaller xmlMarshaller;
  protected final XmlUnmarshaller<T> xmlUnmarshaller;
  protected final String directory;

  protected AbstractXMLRepository(XmlMarshaller xmlMarshaller, XmlUnmarshaller<T> xmlUnmarshaller, String directory) throws IOException {
    this.xmlMarshaller = xmlMarshaller;
    this.xmlUnmarshaller = xmlUnmarshaller;
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

  protected T saveEntity(T entity) throws DataPersistException {
    String fileName = entity.getId() + FILE_EXTENSION;
    Path filePath = Paths.get(directory, fileName);
    Path tmpFilePath = Paths.get(directory, fileName + TMP_EXTENSION);
    try {
      // Marshal the entity to a temporary file
      xmlMarshaller.marshal(entity, tmpFilePath.toFile());
      // Acquire a lock and atomically move the temporary file to the target location
      try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE); FileLock lock = channel.lock()) {
        boolean isNewFile = !Files.exists(filePath);
        Files.move(tmpFilePath, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        if (isNewFile) {
          log.info("Created new {}: {}", entity.getClass().getSimpleName(), filePath);
        } else {
          log.info("Updated {}: {}", entity.getClass().getSimpleName(), filePath);
        }
        return entity;
      }
    } catch (Exception e) {
      throw new DataPersistException("Failed to save " + entityType() + " with ID " + entity.getId(), e);
    } finally {
      try {
        // Ensure temporary file is deleted
        Files.deleteIfExists(tmpFilePath);
      } catch (IOException ex) {
        log.warn("Failed to delete temporary file: {}", tmpFilePath, ex);
      }
    }
  }

  protected List<T> getAllEntities() throws DataReadException {
    List<T> entities = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory), "*" + FILE_EXTENSION)) {
      for (Path filePath : stream) {
        // Skip temporary files
        if (filePath.toString().endsWith(TMP_EXTENSION)) {
          continue;
        }
        try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ); FileLock lock = channel.lock(0L, Long.MAX_VALUE, true)) {
          T entity = xmlUnmarshaller.unmarshal(filePath.toFile());
          if (entity != null) {
            entities.add(entity);
          }
        } catch (Exception e) {
          log.warn("Error retrieving {}: {}, skipping...", entityType(), filePath, e);
        }
      }
    } catch (Exception e) {
      log.warn("Failed to list files in directory: {}", directory, e);
      throw new DataReadException("Failed to retrieve entities from directory: " + directory, e);
    }
    return entities;
  }

  protected void deleteEntity(int id) throws DataDeleteException {
    String fileName = id + FILE_EXTENSION;
    Path filePath = Paths.get(directory, fileName);
    try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE); FileLock lock = channel.lock()) {
      boolean deleted = Files.deleteIfExists(filePath);
      if (deleted) {
        log.info("Deleted {}: {}", entityType(), filePath);
      } else {
        log.warn("{} not found for deletion: {}", entityType(), filePath);
      }
    } catch (NoSuchFileException e) {
      log.warn("{} not found for deletion: {}", entityType(), filePath);
    } catch (Exception e) {
      throw new DataDeleteException("Error deleting " + entityType() + ": " + filePath, e);
    }
  }

  protected String entityType() {
    return "Entity";
  }
}
