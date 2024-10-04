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
import java.util.Set;

/**
 * Represents an event where a user downloads the results of a {@link RequestExecution}. This interface is used for tracking and auditing download
 * activities within the AKTIN research infrastructure.
 *
 * <p>Each download event captures the following information:</p>
 * <ul>
 *     <li><strong>Username</strong>: The user who initiated the download.</li>
 *     <li><strong>User Organizations</strong>: The organizations associated with the user at the time of download.</li>
 *     <li><strong>Download Date</strong>: The exact date and time when the download occurred.</li>
 *     <li><strong>Download Hash</strong>: A cryptographic hash of the downloaded data, used to verify data integrity.</li>
 *     <li><strong>Hash Algorithm</strong>: The algorithm used to generate the download hash (e.g., SHA-1).</li>
 * </ul>
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
