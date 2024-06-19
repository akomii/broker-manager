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
import org.aktin.broker.manager.exceptions.DataReadException;
import org.aktin.broker.manager.models.ManagerRequest;
import org.aktin.broker.manager.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.conf.JaxbConfig;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.filesystem.models.FsSeriesRequest;
import org.aktin.broker.manager.persistence.filesystem.models.FsSingleRequest;
import org.aktin.broker.manager.persistence.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.filesystem.util.XmlUnmarshaller;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.QuerySchedule;
import org.aktin.broker.query.xml.RepeatedExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

class ManagerRequestRepositoryTest {

  @TempDir
  private Path tempDir;

  private ManagerRequestRepository repository;
  private XmlUnmarshaller<ManagerRequest> unmarshaller;

  @BeforeEach
  void setUp() throws IOException, JAXBException {
    XmlMarshaller marshaller = new JaxbConfig().managerRequestXmlMarshaller();
    unmarshaller = new JaxbConfig().managerRequestXmlUnmarshaller();
    repository = new FsManagerRequestRepository(marshaller, unmarshaller, tempDir.toString());
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
    ManagerRequest originalRequest = loadManagerRequestFromTestResources(1);
    int id = repository.save(originalRequest);
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
    ManagerRequest newRequest = loadManagerRequestFromTestResources(1);
    String oldTitle = newRequest.getTitle();
    newRequest.setTitle("ABCDEF");
    int id = repository.save(newRequest);
    assertFalse(areXmlFilesEqual(id));
    newRequest.setTitle(oldTitle);
    repository.save(newRequest);
    assertTrue(areXmlFilesEqual(id));
  }

  @Test
  void testSaveInvalidRequest() {
    ManagerRequest invalidRequest = new FsSingleRequest();
    repository.save(invalidRequest);
  }

  @Test
  void testDelete() {
    copyManagerRequestTestResourceToTempDir(1);
    repository.delete(1);
    Optional<ManagerRequest> deletedRequest = repository.get(1);
    assertFalse(deletedRequest.isPresent());
  }

  @Test
  void testDeleteNotFound() {
    repository.delete(1);
  }

  @Test
  void testGet() {
    copyManagerRequestTestResourceToTempDir(2);
    ManagerRequest expected = loadManagerRequestFromTestResources(2);
    Optional<ManagerRequest> loadedRequest = repository.get(2);
    assertTrue(loadedRequest.isPresent());
    compareManagerRequests(expected, loadedRequest.get());
  }

  // Sample-based comparison, most importantly is that query Extensions are stored and retrieved accordingly
  private void compareManagerRequests(ManagerRequest expected, ManagerRequest actual) {
    assertTrue(FsSeriesRequest.class.isAssignableFrom(expected.getClass()));
    FsSeriesRequest expected1 = (FsSeriesRequest) expected;
    assertTrue(FsSeriesRequest.class.isAssignableFrom(actual.getClass()));
    FsSeriesRequest actual1 = (FsSeriesRequest) actual;
    assertEquals(expected1.getId(), actual1.getId());
    assertEquals(expected1.getTags(), actual1.getTags());
    assertEquals(expected1.getAuthorizedOrganizations(), actual1.getAuthorizedOrganizations());
    assertEquals(expected1.getTargetNodes(), actual1.getTargetNodes());
    assertEquals(expected1.getRequestState(), actual1.getRequestState());
    assertEquals(expected1.getModificationEntries(), actual1.getModificationEntries());
    assertEquals(expected1.getCreatedDate(), actual1.getCreatedDate());
    assertEquals(expected1.getCreatedBy(), actual1.getCreatedBy());
    assertEquals(expected1.getTitle(), actual1.getTitle());
    assertEquals(expected1.getDescription(), actual1.getDescription());
    comparePrincipal(expected1.getPrincipal(), actual1.getPrincipal());
    compareExtensions(expected1.getExtensions(), actual1.getExtensions());
    compareQuerySchedule(expected1.getQuerySchedule(), actual1.getQuerySchedule());
  }

  private void comparePrincipal(Principal expected, Principal actual) {
    assertEquals(expected.name, actual.name);
    assertEquals(expected.organisation, actual.organisation);
    assertEquals(expected.email, actual.email);
    assertEquals(expected.phone, actual.phone);
  }

  private void compareExtensions(List<Element> expected, List<Element> actual) {
    assertEquals(1, expected.size());
    assertEquals(expected.size(), actual.size());
    Element expectedElement = expected.get(0);
    Element actualElement = actual.get(0);
    assertEquals(expectedElement.getTagName(), actualElement.getTagName());
    String expectedContent = expectedElement.getTextContent();
    String actualContent = actualElement.getTextContent();
    assertEquals(expectedContent, actualContent);
  }

  private void compareQuerySchedule(QuerySchedule expected, QuerySchedule actual) {
    assertTrue(RepeatedExecution.class.isAssignableFrom(expected.getClass()));
    RepeatedExecution expected1 = (RepeatedExecution) expected;
    assertTrue(RepeatedExecution.class.isAssignableFrom(actual.getClass()));
    RepeatedExecution actual1 = (RepeatedExecution) actual;
    assertEquals(expected1.id, actual1.id);
    assertEquals(expected1.interval, actual1.interval);
    assertEquals(expected1.intervalHours, actual1.intervalHours);
    assertEquals(expected1.duration, actual1.duration);
  }

  @Test
  void testGetNotFound() {
    Optional<ManagerRequest> request = repository.get(1);
    assertFalse(request.isPresent());
  }

  @Test
  void testGetInvalidRequest() {
    copyManagerRequestTestResourceToTempDir(3);
    assertThrows(DataReadException.class, () -> repository.get(3));
  }

  @Test
  void testGetAllWithValidAndInvalidRequests() {
    copyManagerRequestTestResourceToTempDir(1);
    copyManagerRequestTestResourceToTempDir(2);
    copyManagerRequestTestResourceToTempDir(3);
    List<ManagerRequest> validRequests = repository.getAll();
    assertEquals(2, validRequests.size());
  }

  @Test
  void testGetEmptyDirectory() {
    List<ManagerRequest> allRequests = repository.getAll();
    assertEquals(0, allRequests.size());
  }

  @Test
  void testGetAllForOrganizations() {
    copyManagerRequestTestResourceToTempDir(1);
    copyManagerRequestTestResourceToTempDir(2);
    copyManagerRequestTestResourceToTempDir(3);
    List<ManagerRequest> r1 = repository.getAllForOrganizations(List.of(1));
    assertEquals(2, r1.size());
    List<ManagerRequest> r2 = repository.getAllForOrganizations(List.of(2));
    assertEquals(1, r2.size());
  }

  private String getTestResourcePath(String filename) throws IllegalArgumentException {
    String resourcePath = "requests/" + filename;
    URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      throw new IllegalArgumentException("Test resource not found: " + resourcePath);
    }
    return resourceUrl.getPath();
  }

  private ManagerRequest loadManagerRequestFromTestResources(int id) {
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

  private void copyManagerRequestTestResourceToTempDir(int id) {
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
