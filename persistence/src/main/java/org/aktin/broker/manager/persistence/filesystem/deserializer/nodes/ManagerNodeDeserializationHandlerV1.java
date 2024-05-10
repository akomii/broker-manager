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
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.aktin.broker.manager.persistence.api.models.UserNote;
import org.aktin.broker.manager.persistence.filesystem.deserializer.DeserializationHandler;
import org.aktin.broker.manager.persistence.filesystem.deserializer.MigrationHandler;
import org.aktin.broker.manager.persistence.filesystem.models.ManagerNodeImpl;
import org.aktin.broker.manager.persistence.filesystem.models.UserNoteImpl;

class ManagerNodeDeserializationHandlerV1 extends DeserializationHandler<ManagerNodeImpl> {

  public ManagerNodeDeserializationHandlerV1(MigrationHandler<ManagerNodeImpl> migrationHandlerChain) {
    super(migrationHandlerChain);
  }

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected ManagerNodeImpl doSerialization(JsonNode node) {
    ManagerNodeImpl managerNode = new ManagerNodeImpl();
    managerNode.setDataVersion(getVersion());
    managerNode.setApiKey(node.get("apiKey").asText());
    managerNode.setTags(deserializeTags(node.get("tags")));
    managerNode.setUserNotes(deserializeUserNotes(node.get("userNotes")));
    managerNode.setId(node.get("id").asInt());
    managerNode.setClientDN(node.get("clientDN").asText());
    managerNode.setLastContact(Instant.parse(node.get("lastContact").asText()));
    return managerNode;
  }

  private Set<String> deserializeTags(JsonNode tagsNode) {
    Set<String> tags = new HashSet<>();
    if (tagsNode.isArray()) {
      for (JsonNode tagNode : tagsNode) {
        tags.add(tagNode.asText());
      }
    }
    return tags;
  }

  private List<UserNote> deserializeUserNotes(JsonNode userNotesNode) {
    List<UserNote> userNotes = new ArrayList<>();
    if (userNotesNode.isArray()) {
      for (JsonNode userNoteNode : userNotesNode) {
        UserNoteImpl userNote = new UserNoteImpl();
        userNote.setUsername(userNoteNode.get("username").asText());
        userNote.setDate(Instant.parse(userNoteNode.get("lastContact").asText()));
        userNote.setNoteText(userNoteNode.get("noteText").asText());
      }
    }
    return userNotes;
  }
}
