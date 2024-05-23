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

import jakarta.xml.bind.JAXBException;
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
import org.aktin.broker.manager.persistence.filesystem.conf.JaxbConfig;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataValidationException;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemManagerNode;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlMarshaller;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlUnmarshaller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

class ManagerNodeRepositoryTest {

  @TempDir
  private Path tempDir;

  private ManagerNodeRepository repository;
  private XmlUnmarshaller<FilesystemManagerNode> unmarshaller;

  @BeforeEach
  void setUp() throws IOException, JAXBException {
    XmlMarshaller marshaller = new JaxbConfig().managerNodeXmlMarshaller();
    unmarshaller = new JaxbConfig().managerNodeXmlUnmarshaller();
    repository = new FilesystemManagerNodeRepository(marshaller, unmarshaller, tempDir.toString());
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.walk(tempDir)
        .map(Path::toFile)
        .sorted((o1, o2) -> -o1.compareTo(o2))
        .forEach(File::delete);
  }

  @Test
  void testSave() {
    ManagerNode originalNode = loadManagerNodeFromTestResources(1);
    int id = repository.save(originalNode);
    compareXmlFiles(id);
  }

  void compareXmlFiles(int id) {
    String originalFilePath = getTestResourcePath(id + ".xml");
    String savedFilePath = tempDir + File.separator + id + ".xml";
    try {
      File expected = new File(originalFilePath);
      File actual = new File(savedFilePath);
      Diff diff = DiffBuilder.compare(Input.fromFile(expected))
          .withTest(Input.fromFile(actual))
          .ignoreWhitespace()
          .ignoreComments()
          .build();
      assertFalse(diff.hasDifferences());
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSaveInvalidNode() {
    FilesystemManagerNode invalidNode = new FilesystemManagerNode();
    assertThrows(DataPersistException.class, () -> repository.save(invalidNode));
  }

  @Test
  void testSaveNodeWithMissingKeys() {
    ManagerNode incompleteNode = loadManagerNodeFromTestResources(1);
    incompleteNode.setLastContact(null);
    assertThrows(DataPersistException.class, () -> repository.save(incompleteNode));
  }

  @Test
  void testDelete() {
    copyManagerNodeTestResourceToTempDir(1);
    repository.delete(1);
    Optional<ManagerNode> deletedNode = repository.get(1);
    assertFalse(deletedNode.isPresent());
  }

  @Test
  void testDeleteNotFound() {
    repository.delete(1);
  }

  @Test
  void testGet() {
    copyManagerNodeTestResourceToTempDir(1);
    ManagerNode expected = loadManagerNodeFromTestResources(1);
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
  void testGetNodeWithMissingKeys() {
    copyManagerNodeTestResourceToTempDir(3);
    assertThrows(DataReadException.class, () -> repository.get(3));
  }

  @Test
  void testGetAllWithValidAndInvalidNodes() {
    copyManagerNodeTestResourceToTempDir(1);
    copyManagerNodeTestResourceToTempDir(2);
    copyManagerNodeTestResourceToTempDir(3);
    List<ManagerNode> validNodes = repository.getAll();
    assertEquals(1, validNodes.size());
  }

  @Test
  void testGetEmptyDirectory() {
    List<ManagerNode> allNodes = repository.getAll();
    assertEquals(0, allNodes.size());
  }

  private String getTestResourcePath(String filename) throws IllegalArgumentException {
    String resourcePath = "nodes/" + filename;
    URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      throw new IllegalArgumentException("Test resource not found: " + resourcePath);
    }
    return resourceUrl.getPath();
  }

  private ManagerNode loadManagerNodeFromTestResources(int id) {
    String filename = id + ".xml";
    String resourcePath = getTestResourcePath(filename);
    File file = new File(resourcePath);
    try {
      return unmarshaller.unmarshal(file);
    } catch (JAXBException | DataValidationException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void copyManagerNodeTestResourceToTempDir(int id) {
    String filename = id + ".xml";
    String resourcePath = getTestResourcePath(filename);
    try {
      Path tempFile = Files.createFile(Path.of(tempDir + File.separator + filename));
      try (InputStream inputStream = new FileInputStream(resourcePath)) {
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
