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

package org.aktin.broker.manager.persistence.api.repositories;

import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;

/**
 * Defines an interface for data persistence operations related to {@link ManagerNode} entities.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerNodeRepository {

  /**
   * Saves or updates the provided {@link ManagerNode} entity in persistent storage.
   *
   * @param entity The {@link ManagerNode} object to be saved or updated
   * @return The ID of the saved or updated {@link ManagerNode}.
   * @throws DataPersistException If there are general problems during the save or update operation
   */
  int save(ManagerNode entity) throws DataPersistException;

  /**
   * Deletes the {@link ManagerNode} with the specified ID from persistent storage.
   *
   * @param id The ID of the {@link ManagerNode} to delete
   * @throws DataDeleteException If there's an error while deleting the {@link ManagerNode}
   */
  void delete(int id) throws DataDeleteException;

  /**
   * Retrieves a {@link ManagerNode} from persistent storage based on its ID.
   *
   * @param id The ID of the {@link ManagerNode} to retrieve
   * @return An Optional containing the {@link ManagerNode} if found, or an empty Optional if not
   * @throws DataReadException If there's an error while reading the {@link ManagerNode} data
   */
  Optional<ManagerNode> get(int id) throws DataReadException;

  /**
   * Retrieves all {@link ManagerNode}s from persistent storage.
   *
   * @return A List containing the {@link ManagerNode}s, an empty list if none exist
   * @throws DataReadException If there's an error while reading the {@link ManagerNode} data
   */
  List<ManagerNode> getAll() throws DataReadException;
}
