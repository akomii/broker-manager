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
import java.util.Set;

/**
 * Represents a single event where a user downloads the results of a {@link RequestExecution}. This interface is used for tracking and auditing
 * download activity within the AKTIN research infrastructure. Each download event captures the following information:
 * <ul>
 *     <li><code>Username</code>: The user who initiated the download.</li>
 *     <li><code>User Organizations</code>: The organizations the user is associated with at the time of download.</li>
 *     <li><code>Download Date</code>: The exact date and time when the download occurred.</li>
 *     <li><code>Download Hash</code>: A cryptographic hash of the downloaded data, used to verify data integrity.</li>
 *     <li><code>Hash Algorithm</code>: The algorithm used to generate the download hash (e.g., SHA-1).</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface DownloadEvent {

  /**
   * Gets the username of the user who initiated the download.
   *
   * @return the username
   */
  String getUsername();

  /**
   * Sets the username of the user who initiated the download.
   *
   * @param username the username
   */
  void setUsername(String username);

  /**
   * Gets the set of organizations associated with the user at the time of download.
   *
   * @return the user's organizations
   */
  Set<String> getUserOrganizations();

  /**
   * Sets the set of organizations associated with the user at the time of download.
   *
   * @param userOrganizations the user's organizations
   */
  void setUserOrganizations(Set<String> userOrganizations);

  /**
   * Gets the date and time when the download occurred.
   *
   * @return the download date and time
   */
  Instant getDownloadDate();

  /**
   * Sets the date and time when the download occurred.
   *
   * @param downloadDate the download date and time
   */
  void setDownloadDate(Instant downloadDate);

  /**
   * Gets the cryptographic hash of the downloaded data.
   *
   * @return the download hash
   */
  String getDownloadHash();

  /**
   * Sets the cryptographic hash of the downloaded data.
   *
   * @param downloadHash the download hash
   */
  void setDownloadHash(String downloadHash);

  /**
   * Gets the name of the hash algorithm used to generate the download hash.
   *
   * @return the hash algorithm name
   */
  String getHashAlgorithm();

  /**
   * Sets the name of the hash algorithm used to generate the download hash.
   *
   * @param hashAlgorithm the hash algorithm name
   */
  void setHashAlgorithm(String hashAlgorithm);
}
