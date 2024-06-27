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

package org.aktin.broker.manager.persistence.exceptions;

import org.aktin.broker.manager.model.models.ManagerRequest;
import org.aktin.broker.manager.persistence.repositories.ManagerRequestArchive;

/**
 * This exception is used to wrap any exceptions that occur during the archival process. It is specifically intended for use with archiving operations
 * related to {@link ManagerRequest} inside the {@link ManagerRequestArchive}.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public class DataArchiveException extends RuntimeException {

  /**
   * Constructs a new DataArchiveException with the specified detail message.
   *
   * @param message the detail message explaining the cause of the archival failure.
   */
  public DataArchiveException(String message) {
    super(message);
  }

  /**
   * Constructs a new DataArchiveException with the specified detail message and cause.
   *
   * @param message the detail message explaining the cause of the archival failure.
   * @param cause   the underlying cause of the exception.
   */
  public DataArchiveException(String message, Throwable cause) {
    super(message, cause);
  }
}
