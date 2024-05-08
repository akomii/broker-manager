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

package org.aktin.broker.manager.persistence.filesystem.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FilesystemIdGenerator {

  private int idCounter;

  public FilesystemIdGenerator(Path workingDir) throws IOException {
    this.idCounter = findHighestPersistedId(workingDir);
  }

  private int findHighestPersistedId(Path basePath) throws IOException {
    try (Stream<Path> paths = Files.walk(basePath)) {
      return paths.filter(Files::isRegularFile)
          .map(Path::getFileName)
          .map(Path::toString)
          .filter(name -> name.endsWith(".json"))
          .map(name -> name.substring(0, name.length() - 5)) // Remove ".json" extension
          .mapToInt(Integer::parseInt)
          .max()
          .orElse(0);
    }
  }

  public int generateId() {
    if (idCounter == Integer.MAX_VALUE) {
      throw new IllegalStateException("ID generation limit exceeded");
    }
    return ++idCounter;
  }
}
