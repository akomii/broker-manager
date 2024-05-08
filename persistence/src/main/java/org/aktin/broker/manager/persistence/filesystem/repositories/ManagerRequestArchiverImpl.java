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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.aktin.broker.manager.persistence.api.exceptions.ArchiveDataException;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestArchiver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerRequestArchiverImpl implements ManagerRequestArchiver {

  @Value("${broker-manager.storage.directory.requests}")
  private String storageDirectory;

  @Value("${broker-manager.storage.directory.archive}")
  private String archiveDirectory;

  public ManagerRequestArchiverImpl() throws IOException {
    Files.createDirectories(Paths.get(archiveDirectory));
  }

  public ManagerRequestArchiverImpl(String storageDirectory, String archiveDirectory) throws IOException {
    this.storageDirectory = storageDirectory;
    this.archiveDirectory = archiveDirectory;
    Files.createDirectories(Paths.get(archiveDirectory));
  }

  @Override
  public void archive(int id) throws ArchiveDataException {
    Path sourceFile = Paths.get(storageDirectory, id + ".json");
    Path destinationFile = Paths.get(archiveDirectory, id + ".json");
    try {
      Files.move(sourceFile, destinationFile);
    } catch (IOException e) {
      throw new ArchiveDataException("Failed to archive ManagerRequest with ID: " + id, e);
    }
  }
}