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

package org.aktin.broker.manager.persistence.repositories;

import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.models.ManagerRequest;
import org.aktin.broker.manager.persistence.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.exceptions.DataReadException;

/**
 * Defines an interface for data persistence operations related to {@link ManagerRequest} entities.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequestRepository {

  /**
   * Saves or updates the provided {@link ManagerRequest} entity in persistent storage.
   *
   * @param entity The {@link ManagerRequest} object to be saved or updated
   * @return The ID of the saved or updated {@link ManagerRequest}.
   * @throws DataPersistException If there are general problems during the save or update operation
   */
  int save(ManagerRequest entity) throws DataPersistException;

  /**
   * Deletes the {@link ManagerRequest} with the specified ID from persistent storage.
   *
   * @param id The ID of the {@link ManagerRequest} to delete
   * @throws DataDeleteException If there's an error while deleting the {@link ManagerRequest}
   */
  void delete(int id) throws DataDeleteException;

  /**
   * Retrieves a {@link ManagerRequest} from persistent storage based on its ID.
   *
   * @param id The ID of the {@link ManagerRequest} to retrieve
   * @return An Optional containing the {@link ManagerRequest} if found, or an empty Optional if not
   * @throws DataReadException If there's an error while reading the {@link ManagerRequest} data
   */
  Optional<ManagerRequest> get(int id) throws DataReadException;

  /**
   * Retrieves all {@link ManagerRequest}s from persistent storage.
   *
   * @return A List containing the retrieved {@link ManagerRequest}s, potentially an empty list if none exist or all entities are invalid.
   */
  List<ManagerRequest> getAll();

  /**
   * Retrieves all {@link ManagerRequest}s from persistent storage with corresponding authorized user organization.
   *
   * @return A List containing the retrieved {@link ManagerRequest}s, potentially an empty list if none exist or all entities are invalid.
   */
  List<ManagerRequest> getAllForOrganizations(List<Integer> authorizedOrgIds);
}
