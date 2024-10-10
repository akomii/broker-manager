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
import java.util.List;
import java.util.Set;

/**
 * Represents a participating institution (node) in the AKTIN research infrastructure. Nodes can be targeted by {@link ManagerRequest} to execute data
 * queries and collect research data.
 *
 * <p>The node includes:</p>
 * <ul>
 *   <li><strong>Active State</strong>: Only an active node is visible and can receive {@link ManagerRequest}s.</li>
 *   <li><strong>Tags</strong>: Used to categorize or group the node based on specific criteria.</li>
 *   <li><strong>User Notes</strong>: Additional information or comments.</li>
 *   <li><strong>Client Distinguished Name (DN)</strong>: Human-readable identity verification.</li>
 *   <li><strong>Last Contact Timestamp</strong>: The timestamp of the last communication with the AKTIN query broker.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerNode extends PersistentObject<Integer> {

  boolean isActive();

  void setActive(boolean active);

  Set<String> getTags();

  void setTags(Set<String> tags);

  void addTag(String tag);

  void removeTag(String tag);

  List<TextEntry> getUserNotes();

  void setUserNotes(List<TextEntry> userNotes);

  void addUserNote(TextEntry userNote);

  void removeUserNote(TextEntry userNote);

  String getClientDN();

  void setClientDN(String clientDN);

  Instant getLastContact();

  void setLastContact(Instant lastContact);
}
