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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.filesystem.conf.JacksonConfig;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemManagerNode;
import org.aktin.broker.manager.persistence.filesystem.validation.ManagerNodeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ManagerNodeRepositoryTest {

  @TempDir
  private Path tempDir;

  private ManagerNodeRepository repository;
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() throws IOException {
    mapper = new JacksonConfig().objectMapper();
    repository = new FilesystemManagerNodeRepository(mapper, tempDir.toString(), new ManagerNodeValidator());
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.walk(tempDir)
        .map(Path::toFile)
        .sorted((o1, o2) -> -o1.compareTo(o2))
        .forEach(File::delete);
  }

  @Test
  void testSaveAndGet() throws IOException {
    ManagerNode node = loadManagerNodeFromJson("1.json");
    int id = repository.save(node);
    Optional<ManagerNode> readNode = repository.get(id);
    assertTrue(readNode.isPresent());
    assertEquals(node, readNode.get());
  }

  @Test
  void testSaveInvalidNode() {
    FilesystemManagerNode invalidNode = new FilesystemManagerNode();
    assertThrows(DataPersistException.class, () -> repository.save(invalidNode));
  }

  @Test
  void testSaveNodeWithMissingKeys() throws IOException {
    ManagerNode incompleteNode = loadManagerNodeFromJson("1.json");
    incompleteNode.setLastContact(null);
    assertThrows(DataPersistException.class, () -> repository.save(incompleteNode));
  }

  @Test
  void testDelete() throws IOException {
    copyManagerNodeToTempDir("1.json");
    repository.delete(1);
    Optional<ManagerNode> deletedNode = repository.get(1);
    assertFalse(deletedNode.isPresent());
  }

  @Test
  void testDeleteNotFound() {
    repository.delete(1);
  }

  @Test
  void testGetNotFound() {
    Optional<ManagerNode> node = repository.get(1);
    assertFalse(node.isPresent());
  }

  @Test
  void testGetNodeWithMissingKeys() throws IOException {
    copyManagerNodeToTempDir("3.json");
    assertThrows(DataReadException.class, () -> repository.get(3));
  }

  @Test
  void testGetAllWithValidAndInvalidNodes() throws IOException {
    copyManagerNodeToTempDir("1.json");
    copyManagerNodeToTempDir("2.json");
    copyManagerNodeToTempDir("3.json");
    List<ManagerNode> validNodes = repository.getAll();
    assertEquals(1, validNodes.size());
  }

  @Test
  void testGetEmptyDirectory() {
    List<ManagerNode> allNodes = repository.getAll();
    assertEquals(0, allNodes.size());
  }

  private ManagerNode loadManagerNodeFromJson(String filename) throws IOException {
    String resourcePath = "nodes/" + filename;
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("Resource not found: " + resourcePath);
      }
      return mapper.readValue(inputStream, ManagerNode.class);
    }
  }

  private void copyManagerNodeToTempDir(String filename) throws IOException {
    String resourcePath = "nodes/" + filename;
    Path tempFile = Files.createFile(Path.of(tempDir + File.separator + filename));
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("Resource not found: " + resourcePath);
      }
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
    }
  }
}
