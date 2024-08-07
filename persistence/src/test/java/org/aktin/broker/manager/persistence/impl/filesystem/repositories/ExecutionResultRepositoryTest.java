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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExecutionResultRepositoryTest {

  @TempDir
  private Path tempDir;

  private ExecutionResultRepository repository;

  @BeforeEach
  void setUp() throws IOException {
    repository = new FsExecutionResultRepository(tempDir.toString());
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
    InputStream is = getExecutionResultTestResourceAsInputStream("export_1.zip");
    repository.save(is, "file1.txt");
    assertTrue(areBinaryFilesEqual(Path.of(getTestResourcePath("export_1.zip")), Path.of(String.valueOf(tempDir), "file1.txt")));
  }

  @Test
  void testSaveDuplicate() throws FileNotFoundException {
    copyExecutionResultTestResourceToTempDir("export_1.zip");
    InputStream is = getExecutionResultTestResourceAsInputStream("export_1.zip");
    assertThrows(DataPersistException.class, () -> repository.save(is, "export_1.zip"));
  }

  @Test
  void testSaveInvalid() throws IOException {
    InputStream is = getExecutionResultTestResourceAsInputStream("export_2.zip");
    repository.save(is, "export_2.zip");
  }

  @Test
  void testDelete() {
    copyExecutionResultTestResourceToTempDir("export_1.zip");
    repository.delete("export_1.zip");
    Optional<InputStream> deletedResult = repository.get("export_1.zip");
    assertFalse(deletedResult.isPresent());
  }

  @Test
  void testDeleteNotFound() {
    repository.delete("export_1.zip");
  }

  @Test
  void testGet() throws IOException {
    copyExecutionResultTestResourceToTempDir("export_1.zip");
    Optional<InputStream> opt = repository.get("export_1.zip");
    assertTrue(opt.isPresent());
    InputStream is = opt.get();
    assertTrue(isZip(is));
  }

  public boolean isZip(InputStream inputStream) throws IOException {
    byte[] zipSignature = {0x50, 0x4b, 0x03, 0x04};
    byte[] signature = new byte[zipSignature.length];
    inputStream.read(signature);
    inputStream.close();
    for (int i = 0; i < zipSignature.length; i++) {
      if (signature[i] != zipSignature[i]) {
        return false;
      }
    }
    return true;
  }

  @Test
  void testGetNotFound() {
    Optional<InputStream> opt = repository.get("export_2.zip");
    assertFalse(opt.isPresent());
  }

  @Test
  void testGetInvalidRequest() {
    copyExecutionResultTestResourceToTempDir("export_1.zip");
    repository.get("export_1.zip");
  }

  private String getTestResourcePath(String filename) throws IllegalArgumentException {
    String resourcePath = "results/" + filename;
    URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      throw new IllegalArgumentException("Test resource not found: " + resourcePath);
    }
    return resourceUrl.getPath();
  }

  private void copyExecutionResultTestResourceToTempDir(String filename) {
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

  private InputStream getExecutionResultTestResourceAsInputStream(String filename) throws FileNotFoundException {
    String resourcePath = getTestResourcePath(filename);
    return new FileInputStream(resourcePath);
  }

  private boolean areBinaryFilesEqual(Path file1Path, Path file2Path) throws IOException {
    return Files.mismatch(file1Path, file2Path) == -1;
  }
}
