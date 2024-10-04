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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataPersistException;

public class FsExecutionResultRepository extends AbstractBinaryRepository implements ExecutionResultRepository {

  public FsExecutionResultRepository(String resultsDirectory) throws IOException {
    super(resultsDirectory);
  }

  @Override
  public void write(InputStream result, String identifier) throws DataPersistException {
    Path filePath = Paths.get(directory, identifier);
    if (Files.exists(filePath)) {
      throw new DataPersistException("Execution result must be unique. File already exists: " + filePath);
    }
    saveData(result, identifier);
  }

  @Override
  public Optional<InputStream> get(String identifier) {
    return getData(identifier);
  }

  @Override
  public void delete(String identifier) {
    deleteData(identifier);
  }
}
