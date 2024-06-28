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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataPersistException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;

/**
 * Defines an interface for persisting operations related to results of {@link RequestExecution} entities.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ExecutionResultRepository {

  /**
   * Saves the provided result data as an {@link InputStream} and returns a unique filename associated with the saved result.
   *
   * @param result   the {@link InputStream} containing the result data to be saved
   * @param filename the name of the result data to store as
   * @throws DataPersistException if an error occurs while saving the result data
   */
  void save(InputStream result, String filename) throws DataPersistException;

  /**
   * Retrieves the result data associated with the provided filename as an {@link InputStream}.
   *
   * @param filename the unique filename associated with the result data to be retrieved
   * @return an {@link InputStream} containing the result data
   * @throws DataReadException if an error occurs while retrieving the result data
   */
  Optional<InputStream> get(String filename) throws DataReadException;

  /**
   * Deletes the result data associated with the provided filename.
   *
   * @param filename the unique filename associated with the result data to be deleted
   * @throws DataDeleteException if an error occurs while deleting the result data
   */
  void delete(String filename) throws DataDeleteException;
}