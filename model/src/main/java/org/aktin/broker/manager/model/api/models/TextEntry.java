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

package org.aktin.broker.manager.model.api.models;

import java.time.Instant;

/**
 * Represents a textual entry associated with a user, such as a note or modification entry. Implementations of this interface encapsulate information
 * about the author, creation date, and content of the text entry. Examples of text entries are user notes in {@link ManagerNode} or modification
 * entries in {@link ManagerRequest}.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface TextEntry {

  /**
   * Returns the username of the author of this text entry.
   *
   * @return the username of the author.
   */
  String getUsername();

  /**
   * Sets the username of the author of this text entry.
   *
   * @param username the username of the author.
   */
  void setUsername(String username);

  /**
   * Returns the creation date of this text entry.
   *
   * @return the creation date as an {@link Instant}.
   */
  Instant getCreatedDate();

  /**
   * Sets the creation date of this text entry.
   *
   * @param createdDate the creation date as an {@link Instant}.
   */
  void setCreatedDate(Instant createdDate);

  /**
   * Returns the content of this text entry.
   *
   * @return the content of the text entry.
   */
  String getContent();

  /**
   * Sets the content of this text entry.
   *
   * @param content the content of the text entry.
   */
  void setContent(String content);
}
