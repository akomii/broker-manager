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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.aktin.broker.manager.persistence.exceptions.DataReadException;
import org.aktin.broker.manager.model.models.ManagerNode;
import org.aktin.broker.manager.persistence.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.impl.filesystem.conf.JaxbConfig;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.impl.filesystem.models.FsManagerNode;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

class ManagerNodeRepositoryTest {

  @TempDir
  private Path tempDir;

  private ManagerNodeRepository repository;
  private XmlUnmarshaller<ManagerNode> unmarshaller;

  @BeforeEach
  void setUp() throws IOException, JAXBException {
    XmlMarshaller marshaller = new JaxbConfig().managerNodeXmlMarshaller();
    unmarshaller = new JaxbConfig().managerNodeXmlUnmarshaller();
    repository = new FsManagerNodeRepository(marshaller, unmarshaller, tempDir.toString());
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
    assertTrue(areXmlFilesEqual(id));
  }

  private boolean areXmlFilesEqual(int id) {
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
      return !diff.hasDifferences();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Test
  void testUpdate() {
    ManagerNode newNode = loadManagerNodeFromTestResources(1);
    String oldKey = newNode.getApiKey();
    newNode.setApiKey("ABDCDEF");
    int id = repository.save(newNode);
    assertFalse(areXmlFilesEqual(id));
    newNode.setApiKey(oldKey);
    repository.save(newNode);
    assertTrue(areXmlFilesEqual(id));
  }

  @Test
  void testSaveInvalidNode() {
    FsManagerNode invalidNode = new FsManagerNode();
    assertThrows(NullPointerException.class, () -> repository.save(invalidNode));
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
    compareManagerNodes(expected, loadedNode.get());
  }

  private void compareManagerNodes(ManagerNode expected, ManagerNode actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getApiKey(), actual.getApiKey());
    assertEquals(expected.getTags(), actual.getTags());
    assertEquals(expected.getUserNotes(), actual.getUserNotes());
    assertEquals(expected.getLastContact(), actual.getLastContact());
    assertEquals(expected.getClientDN(), actual.getClientDN());
  }

  @Test
  void testGetNotFound() {
    Optional<ManagerNode> node = repository.get(1);
    assertFalse(node.isPresent());
  }

  @Test
  void testGetInvalidNode() {
    copyManagerNodeTestResourceToTempDir(3);
    assertThrows(DataReadException.class, () -> repository.get(3));
  }

  @Test
  void testGetAllWithValidAndInvalidNodes() {
    copyManagerNodeTestResourceToTempDir(1);
    copyManagerNodeTestResourceToTempDir(2);
    copyManagerNodeTestResourceToTempDir(3);
    List<ManagerNode> validNodes = repository.getAll();
    assertEquals(2, validNodes.size());
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
    } catch (JAXBException | DataMigrationException | IllegalArgumentException | IOException | SAXException | ParserConfigurationException e) {
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
