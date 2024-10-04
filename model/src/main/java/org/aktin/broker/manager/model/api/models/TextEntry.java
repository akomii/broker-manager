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
 * Represents a textual entry associated with a user, such as a note or modification log.
 *
 * <p>Text entries include information about:</p>
 * <ul>
 *   <li><strong>Author</strong>: The username of the person who created the entry.</li>
 *   <li><strong>Creation Date</strong>: When the entry was made.</li>
 *   <li><strong>Content</strong>: The actual text content of the entry.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface TextEntry {

  String getUsername();

  void setUsername(String username);

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getContent();

  void setContent(String content);
}