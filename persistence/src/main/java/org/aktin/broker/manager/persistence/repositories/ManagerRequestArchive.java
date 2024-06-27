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

import java.util.Optional;
import org.aktin.broker.manager.model.models.ManagerRequest;
import org.aktin.broker.manager.persistence.exceptions.DataArchiveException;
import org.aktin.broker.manager.persistence.exceptions.DataReadException;

/**
 * Enables the relocation of {@link ManagerRequest} from primary data storage to designated archive storages to manage storage resources and
 * historical data.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequestArchive {

  /**
   * Moves a persisted {@link ManagerRequest} to an archive storage location.
   *
   * @param id The unique identifier of the {@link ManagerRequest} to archive
   * @return The ID of the archived {@link ManagerRequest}
   * @throws DataArchiveException If an error occurs during the archival process
   */
  int archive(int id) throws DataArchiveException;

  /**
   * Retrieves a {@link ManagerRequest} from archive storage based on its ID.
   *
   * @param id The ID of the {@link ManagerRequest} to retrieve
   * @return An Optional containing the {@link ManagerRequest} if found, or an empty Optional if not
   * @throws DataReadException If there's an error while reading the {@link ManagerRequest} data
   */
  Optional<ManagerRequest> get(int id) throws DataReadException;
}
