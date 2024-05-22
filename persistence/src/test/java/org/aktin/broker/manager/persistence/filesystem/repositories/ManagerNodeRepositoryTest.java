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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import org.aktin.broker.manager.persistence.filesystem.conf.PersistenceConfig;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemManagerNode;
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
    Validator validator = new PersistenceConfig().validator();
    mapper = new JacksonConfig().objectMapper();
    repository = new FilesystemManagerNodeRepository(mapper, validator, tempDir.toString());
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.walk(tempDir)
        .map(Path::toFile)
        .sorted((o1, o2) -> -o1.compareTo(o2))
        .forEach(File::delete);
  }

  @Test
  void testSave() throws IOException {
    ManagerNode originalNode = loadManagerNodeFromJson("1.json");
    int id = repository.save(originalNode);
    compareJsonFiles(id);
  }

  void compareJsonFiles(int id) throws IOException {
    String originalFilePath = getTestResourcePath("1.json");
    String savedFilePath = tempDir + "/" + id + ".json";
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode normalizedOriginal = objectMapper.readTree(new File(originalFilePath));
    JsonNode normalizedSaved = objectMapper.readTree(new File(savedFilePath));
    assertEquals(normalizedOriginal, normalizedSaved);
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
    copyJsonToTempDir("1.json");
    repository.delete(1);
    Optional<ManagerNode> deletedNode = repository.get(1);
    assertFalse(deletedNode.isPresent());
  }

  @Test
  void testDeleteNotFound() {
    repository.delete(1);
  }

  @Test
  void testGet() throws IOException {
    copyJsonToTempDir("1.json");
    ManagerNode expected = loadManagerNodeFromJson("1.json");
    Optional<ManagerNode> loadedNode = repository.get(1);
    assertTrue(loadedNode.isPresent());
    assertEquals(expected, loadedNode.get());
  }

  @Test
  void testGetNotFound() {
    Optional<ManagerNode> node = repository.get(1);
    assertFalse(node.isPresent());
  }

  @Test
  void testGetNodeWithMissingKeys() throws IOException {
    copyJsonToTempDir("3.json");
    assertThrows(DataReadException.class, () -> repository.get(3));
  }

  @Test
  void testGetAllWithValidAndInvalidNodes() throws IOException {
    copyJsonToTempDir("1.json");
    copyJsonToTempDir("2.json");
    copyJsonToTempDir("3.json");
    List<ManagerNode> validNodes = repository.getAll();
    assertEquals(1, validNodes.size());
  }

  @Test
  void testGetEmptyDirectory() {
    List<ManagerNode> allNodes = repository.getAll();
    assertEquals(0, allNodes.size());
  }

  private String getTestResourcePath(String filename) {
    String resourcePath = "nodes/" + filename;
    URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      throw new IllegalArgumentException("Test resource not found: " + resourcePath);
    }
    return resourceUrl.getPath();
  }

  private ManagerNode loadManagerNodeFromJson(String filename) throws IOException {
    String resourcePath = getTestResourcePath(filename);
    try (InputStream inputStream = new FileInputStream(resourcePath)) {
      return mapper.readValue(inputStream, ManagerNode.class);
    }
  }

  private void copyJsonToTempDir(String filename) throws IOException {
    String resourcePath = getTestResourcePath(filename);
    Path tempFile = Files.createFile(Path.of(tempDir + File.separator + filename));
    try (InputStream inputStream = new FileInputStream(resourcePath)) {
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
    }
  }
}
