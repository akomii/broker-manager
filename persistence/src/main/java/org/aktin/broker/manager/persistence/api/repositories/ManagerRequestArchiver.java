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


import org.aktin.broker.manager.persistence.api.exceptions.ArchiveDataException;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;

/**
 * Enables the relocation of {@link ManagerRequest} from primary data storage to designated archive storages to manage storage resources and
 * historical data.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequestArchiver {

  /**
   * Moves a persisted {@link ManagerRequest} to an archive storage location.
   *
   * @param id The ID of the {@link ManagerRequest} object to move to the archive
   * @throws ArchiveDataException If there's an error during the archival process
   */
  void archive(int id) throws ArchiveDataException;
}
