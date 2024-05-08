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
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.aktin.broker.manager.persistence.api.exceptions.DeletePersistedDataException;
import org.aktin.broker.manager.persistence.api.exceptions.PersistDataException;
import org.aktin.broker.manager.persistence.api.exceptions.ReadPersistedDataException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.filesystem.models.ManagerNodeImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

// TODO create merged abstract class together with RequestHandler
@Repository
public class ManagerNodeRepositoryImpl implements ManagerNodeRepository {

  @Value("${broker-manager.storage.directory.nodes}")
  private String storageDirectory;

  private final ObjectMapper mapper = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT)
      .registerModule(new JavaTimeModule());

  public ManagerNodeRepositoryImpl() throws IOException {
    Files.createDirectories(Paths.get(this.storageDirectory));
  }

  public ManagerNodeRepositoryImpl(String storageDirectory) throws IOException {
    this.storageDirectory = storageDirectory;
    Files.createDirectories(Paths.get(this.storageDirectory));
  }

  @Override
  public void save(ManagerNode entity) throws PersistDataException {
    try {
      String filename = storageDirectory + "/" + entity.getId() + ".json";
      mapper.writeValue(new File(filename), entity);
    } catch (IOException e) {
      throw new PersistDataException("Failed to save ManagerNode: " + entity.getId(), e);
    }
  }

  @Override
  public void delete(int id) throws DeletePersistedDataException {
    try {
      String filename = storageDirectory + "/" + id + ".json";
      Files.deleteIfExists(Paths.get(filename));
    } catch (IOException e) {
      throw new DeletePersistedDataException("Failed to delete ManagerNode: " + id, e);
    }
  }

  @Override
  public Optional<ManagerNode> get(int id) throws ReadPersistedDataException {
    String filename = storageDirectory + "/" + id + ".json";
    File file = new File(filename);
    return file.exists() ? deserializeManagerNode(file) : Optional.empty();
  }

  @Override
  public List<ManagerNode> getAll() throws ReadPersistedDataException {
    File storageDir = new File(storageDirectory);
    return Arrays.stream(storageDir.listFiles((dir, name) -> name.endsWith(".json")))
        .map(this::deserializeManagerNode)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private Optional<ManagerNode> deserializeManagerNode(File file) {
    try {
      return Optional.of(mapper.readValue(file, ManagerNodeImpl.class));
    } catch (IOException e) {
      throw new ReadPersistedDataException("Error reading ManagerNode from file: " + file.getName(), e);
    }
  }
}
