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
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.model.api.models.PersistentObject;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractXMLRepository<T extends PersistentObject> extends AbstractBinaryRepository {

  private static final Logger log = LoggerFactory.getLogger(AbstractXMLRepository.class);
  private static final String FILE_EXTENSION = ".xml";

  protected final XmlMarshaller xmlMarshaller;
  protected final XmlUnmarshaller<T> xmlUnmarshaller;

  protected AbstractXMLRepository(XmlMarshaller xmlMarshaller, XmlUnmarshaller<T> xmlUnmarshaller, String directory) throws IOException {
    super(directory);
    this.xmlMarshaller = xmlMarshaller;
    this.xmlUnmarshaller = xmlUnmarshaller;
  }

  protected T saveXML(T entity) throws DataPersistException {
    String filename = entity.getId() + FILE_EXTENSION;
    Path tmpFilePath = Paths.get(directory, filename + TMP_EXTENSION);
    try {
      // Marshal the entity to a temporary file
      xmlMarshaller.marshal(entity, tmpFilePath.toFile());
      Path filePath = Paths.get(directory, filename);
      moveTempFile(filePath, tmpFilePath);
      return entity;
    } catch (Exception e) {
      throw new DataPersistException("Failed to save " + entityType() + " with ID " + entity.getId(), e);
    }
  }

  protected Optional<T> getXML(String filename) throws DataReadException {
    Optional<InputStream> fileStream = getFile(filename);
    if (fileStream.isPresent()) {
      try (InputStream inputStream = fileStream.get()) {
        T entity = xmlUnmarshaller.unmarshal(inputStream);
        return Optional.of(entity);
      } catch (Exception e) {
        throw new DataReadException("Error unmarshalling XML from file: " + filename, e);
      }
    }
    return Optional.empty();
  }

  protected List<T> getAllXMLs() throws DataReadException {
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
    } catch (IOException e) {
      throw new DataReadException("Error reading files from directory: " + directory, e);
    }
    return entities;
  }

  protected void deleteXML(int id) throws DataDeleteException {
    String filename = id + FILE_EXTENSION;
    deleteFile(filename);
  }

  protected String entityType() {
    return "Entity";
  }
}
