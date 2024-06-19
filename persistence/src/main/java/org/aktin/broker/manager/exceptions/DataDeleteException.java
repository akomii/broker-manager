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

package org.aktin.broker.manager.exceptions;

/**
 * This exception is used to wrap any exceptions that occur during delete operations across all
 * {@link org.aktin.broker.manager.repositories}, providing a way to signal issues specifically related to data deletion processes within
 * the application.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public class DataDeleteException extends RuntimeException {

  /**
   * Constructs a new DataDeleteException with the specified detail message.
   *
   * @param message the detail message explaining the cause of the deletion failure.
   */
  public DataDeleteException(String message) {
    super(message);
  }

  /**
   * Constructs a new DataDeleteException with the specified detail message and cause.
   *
   * @param message the detail message explaining the cause of the deletion failure.
   * @param cause   the underlying cause of the exception.
   */
  public DataDeleteException(String message, Throwable cause) {
    super(message, cause);
  }
}
