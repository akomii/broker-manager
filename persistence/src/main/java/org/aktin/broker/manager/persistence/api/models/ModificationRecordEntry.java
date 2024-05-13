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

package org.aktin.broker.manager.persistence.api.models;

import java.time.Instant;

/**
 * Represents a single entry in the modification history of a {@link ManagerRequest}. This class is used for tracking and auditing the modified
 * content done by user.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ModificationRecordEntry {

  Instant getDate();

  void setDate(Instant date);

  String getUsername();

  void setUsername(String username);

  String getClob();

  void setClob(String clob);
}
