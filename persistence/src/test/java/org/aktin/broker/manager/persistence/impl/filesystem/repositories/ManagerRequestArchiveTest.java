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
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.impl.filesystem.conf.JaxbConfig;
import org.aktin.broker.manager.persistence.impl.filesystem.repositories.FsManagerRequestArchive;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestArchive;
import org.aktin.broker.manager.persistence.api.exceptions.DataArchiveException;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.impl.filesystem.models.FsSeriesRequest;
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

class ManagerRequestArchiveTest {

  @TempDir
  private Path tempArchiveDir;

  @TempDir
  private Path tempRequestsDir;

  private XmlUnmarshaller<ManagerRequest> unmarshaller;
  private ManagerRequestArchive archive;

  @BeforeEach
  void setUp() throws IOException, JAXBException {
    unmarshaller = new JaxbConfig().managerRequestXmlUnmarshaller();
    archive = new FsManagerRequestArchive(unmarshaller, tempRequestsDir.toString(), tempArchiveDir.toString());
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.walk(tempArchiveDir)
        .map(Path::toFile)
        .sorted((o1, o2) -> -o1.compareTo(o2))
        .forEach(File::delete);
    Files.walk(tempRequestsDir)
        .map(Path::toFile)
        .sorted((o1, o2) -> -o1.compareTo(o2))
        .forEach(File::delete);
  }

  @Test
  void testArchive() throws IOException {
    copyManagerRequestTestResourceToTempDir(1, tempRequestsDir);
    assertEquals(1, countFilesInDirectory(tempRequestsDir));
    assertEquals(0, countFilesInDirectory(tempArchiveDir));
    int id = archive.archive(1);
    assertEquals(0, countFilesInDirectory(tempRequestsDir));
    assertEquals(1, countFilesInDirectory(tempArchiveDir));
    assertTrue(areXmlFilesEqual(id));
  }

  private boolean areXmlFilesEqual(int id) {
    String originalFilePath = getTestResourcePath(id + ".xml");
    String savedFilePath = tempArchiveDir + File.separator + id + ".xml";
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
  void testArchiveNotFound() {
    assertThrows(DataArchiveException.class, () -> archive.archive(1));
  }

  @Test
  void testArchiveInvalidRequest() {
    copyManagerRequestTestResourceToTempDir(3, tempRequestsDir);
    archive.archive(3);
  }

  @Test
  void testGet() {
    copyManagerRequestTestResourceToTempDir(2, tempArchiveDir);
    ManagerRequest expected = loadManagerRequestFromTestResources(2);
    Optional<ManagerRequest> loadedRequest = archive.get(2);
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
    Optional<ManagerRequest> request = archive.get(1);
    assertFalse(request.isPresent());
  }

  @Test
  void testGetInvalidRequest() {
    copyManagerRequestTestResourceToTempDir(3, tempArchiveDir);
    assertThrows(DataReadException.class, () -> archive.get(3));
  }

  private int countFilesInDirectory(Path directoryPath) throws IOException {
    if (!Files.isDirectory(directoryPath)) {
      throw new IllegalArgumentException("Provided path is not a directory: " + directoryPath);
    }
    try (Stream<Path> files = Files.list(directoryPath)) {
      return (int) files.filter(Files::isRegularFile).count();
    }
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

  private void copyManagerRequestTestResourceToTempDir(int id, Path tempDir) {
    String filename = id + ".xml";
    String resourcePath = getTestResourcePath(filename);
    try {
      Path tempFile = Files.createFile(Path.of(tempDir + File.separator + filename));
      try (
          InputStream inputStream = new FileInputStream(resourcePath)) {
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
