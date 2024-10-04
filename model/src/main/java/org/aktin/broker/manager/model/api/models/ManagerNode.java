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
 * Represents a participating institution (node) in the AKTIN reserach infrastructure. Nodes can be targeted by {@link ManagerRequest} to execute data
 * queries and collect research data. Each node is uniquely identified by an ID and may be associated with:
 * <ul>
 *   <li>An API key for authentication.</li>
 *   <li>Tags to categorize or group the node based on specific criteria.</li>
 *   <li>User notes for additional information or comments.</li>
 *   <li>Client DN (Distinguished Name) human-readable identity verification.</li>
 *   <li>Timestamp of the last contact made by the node with the AKTIN query broker.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerNode extends PersistentObject {

  boolean isActive();

  void setActive(boolean active);

  /**
   * Gets the set of tags associated with this manager node.
   *
   * @return the tags
   */
  Set<String> getTags();

  /**
   * Sets the set of tags associated with this manager node.
   *
   * @param tags the tags
   */
  void setTags(Set<String> tags);

  void addTag(String tag);

  void removeTag(String tag);

  /**
   * Gets the list of user notes associated with this manager node.
   *
   * @return the user notes
   */
  List<TextEntry> getUserNotes();

  /**
   * Sets the list of user notes associated with this manager node.
   *
   * @param userNotes the user notes
   */
  void setUserNotes(List<TextEntry> userNotes);

  void addUserNote(TextEntry userNote);

  void removeUserNote(TextEntry userNote);

  /**
   * Gets the Distinguished Name (DN) associated with this manager node.
   *
   * @return the client DN
   */
  String getClientDN();

  /**
   * Sets the Distinguished Name (DN) associated with this manager node.
   *
   * @param clientDN the client DN
   */
  void setClientDN(String clientDN);

  /**
   * Gets the timestamp of the last contact made with the AKTIN query broker.
   *
   * @return the timestamp of last contact
   */
  Instant getLastContact();

  /**
   * Sets the timestamp of the last contact made with the AKTIN query broker.
   *
   * @param lastContact the timestamp of last contact
   */
  void setLastContact(Instant lastContact);
}
