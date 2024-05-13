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

package org.aktin.broker.manager.persistence.filesystem.deserializer.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.aktin.broker.manager.persistence.api.models.UserNote;
import org.aktin.broker.manager.persistence.filesystem.deserializer.DeserializationHandler;
import org.aktin.broker.manager.persistence.filesystem.deserializer.MigrationHandler;
import org.aktin.broker.manager.persistence.filesystem.models.ManagerNodeImpl;
import org.aktin.broker.manager.persistence.filesystem.models.UserNoteImpl;

class ManagerNodeDeserializationHandlerV1 extends DeserializationHandler<ManagerNodeImpl> {

  public ManagerNodeDeserializationHandlerV1(MigrationHandler<ManagerNodeImpl> migrationHandlerChain) {
    super(1, migrationHandlerChain);
  }

  @Override
  protected ManagerNodeImpl doSerialization(JsonNode node) {
    ManagerNodeImpl managerNode = new ManagerNodeImpl();
    managerNode.setDataVersion(getVersion());
    managerNode.setApiKey(deserializeText(node, "apiKey"));
    managerNode.setTags(deserializeTextList(node, "tags"));
    managerNode.setUserNotes(deserializeUserNotes(node));
    managerNode.setId(deserializeNumber(node, "id"));
    managerNode.setClientDN(deserializeText(node, "clientDN"));
    managerNode.setLastContact(deserializeDate(node, "lastContact"));
    return managerNode;
  }

  private List<UserNote> deserializeUserNotes(JsonNode node) {
    JsonNode userNotesNode = node.get("userNotes");
    List<UserNote> userNotes = new ArrayList<>();
    if (userNotesNode != null) {
      for (JsonNode userNoteNode : userNotesNode) {
        UserNoteImpl userNote = new UserNoteImpl();
        userNote.setUsername(deserializeText(userNoteNode, "username"));
        userNote.setDate(deserializeDate(userNoteNode, "date"));
        userNote.setText(deserializeText(userNoteNode, "text"));
        userNotes.add(userNote);
      }
    }
    return userNotes;
  }
}
