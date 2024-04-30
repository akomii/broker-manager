/*
 *    Copyright (c) 2024  AKTIN
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.api.models;

import java.time.Instant;
import java.util.Set;

/**
 * Represents a single event of a user downloading the results of a {@link RequestExecution}. This class is used for tracking and auditing download
 * activity.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface DownloadEvent {

  String getUsername();

  void setUsername(String username);

  Set<String> getUserOrganizations();

  void setUserOrganizations(Set<String> userOrganizations);

  Instant getDownloadDate();

  void setDownloadDate(Instant downloadDate);

  String getDownloadHash();

  void setDownloadHash(String downloadHash);

  String getHashAlgorithm();

  void setHashAlgorithm(String hashAlgorithm);
}
