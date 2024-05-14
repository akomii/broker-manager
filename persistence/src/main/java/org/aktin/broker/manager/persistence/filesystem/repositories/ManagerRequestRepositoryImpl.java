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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.aktin.broker.manager.persistence.api.exceptions.DeletePersistedDataException;
import org.aktin.broker.manager.persistence.api.exceptions.PersistDataException;
import org.aktin.broker.manager.persistence.api.exceptions.ReadPersistedDataException;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.models.AbstractManagerRequest;
import org.aktin.broker.manager.persistence.filesystem.utils.FilesystemIdGenerator;
import org.aktin.broker.query.xml.QuerySchedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerRequestRepositoryImpl implements ManagerRequestRepository {

  @Value("${broker-manager.storage.directory.requests}")
  private String storageDirectory;

  private final ObjectMapper mapper = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT)
      .registerModule(new JavaTimeModule());

  private final FilesystemIdGenerator filesystemIdGenerator;

  public ManagerRequestRepositoryImpl() throws IOException {
    Files.createDirectories(Paths.get(storageDirectory));
    Path storagePath = Paths.get(storageDirectory);
    Files.createDirectories(storagePath);
    filesystemIdGenerator = new FilesystemIdGenerator(storagePath);
  }

  public ManagerRequestRepositoryImpl(String storageDirectory) throws IOException {
    this.storageDirectory = storageDirectory;
    Path storagePath = Paths.get(storageDirectory);
    Files.createDirectories(storagePath);
    filesystemIdGenerator = new FilesystemIdGenerator(storagePath);
  }

  @Override
  public void save(ManagerRequest<QuerySchedule> entity) throws PersistDataException {
    if (entity.getId() == 0) {
      entity.setId(filesystemIdGenerator.generateId());
    }
    try {
      String filename = storageDirectory + "/" + entity.getId() + ".json";
      mapper.writeValue(new File(filename), entity);
    } catch (IOException e) {
      throw new PersistDataException("Failed to save ManagerRequest: " + entity.getId(), e);
    }
  }

  @Override
  public void delete(int id) throws DeletePersistedDataException {
    try {
      String filename = storageDirectory + "/" + id + ".json";
      Files.deleteIfExists(Paths.get(filename));
    } catch (IOException e) {
      throw new DeletePersistedDataException("Failed to delete ManagerRequest: " + id, e);
    }
  }

  @Override
  public Optional<ManagerRequest<QuerySchedule>> get(int id) throws ReadPersistedDataException {
    String filename = storageDirectory + "/" + id + ".json";
    File file = new File(filename);
    return file.exists() ? deserializeManagerRequest(file) : Optional.empty();
  }

  @Override
  public List<ManagerRequest<QuerySchedule>> getAll() throws ReadPersistedDataException {
    File storageDir = new File(storageDirectory);
    return Arrays.stream(storageDir.listFiles((dir, name) -> name.endsWith(".json")))
        .map(this::deserializeManagerRequest)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private Optional<ManagerRequest<QuerySchedule>> deserializeManagerRequest(File file) {
    try {
      return Optional.of(mapper.readValue(file, ManagerRequest.class));
    } catch (IOException e) {
      throw new ReadPersistedDataException("Error reading ManagerRequest from file: " + file.getName(), e);
    }
  }
}
