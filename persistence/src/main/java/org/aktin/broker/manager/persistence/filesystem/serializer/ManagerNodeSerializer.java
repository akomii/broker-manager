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

package org.aktin.broker.manager.persistence.filesystem.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.models.UserNote;

public class ManagerNodeSerializer extends JsonSerializer<ManagerNode> {

  private static final int DATA_VERSION = 1;

  @Override
  public void serialize(ManagerNode managerNode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("dataVersion", DATA_VERSION);
    jsonGenerator.writeStringField("apiKey", managerNode.getApiKey() != null ? managerNode.getApiKey() : "");
    serializeTags(managerNode.getTags(), jsonGenerator);
    serializeUserNote(managerNode.getUserNotes(), jsonGenerator);
    jsonGenerator.writeNumberField("id", managerNode.getId());
    jsonGenerator.writeStringField("clientDN", managerNode.getClientDN());
    jsonGenerator.writeStringField("lastContact", managerNode.getLastContact().toString());
    jsonGenerator.writeEndObject();
  }

  private void serializeTags(Set<String> tags, JsonGenerator jsonGenerator) throws IOException {
    if (tags != null && !tags.isEmpty()) {
      jsonGenerator.writeArrayFieldStart("tags");
      for (String tag : tags) {
        jsonGenerator.writeString(tag);
      }
      jsonGenerator.writeEndArray();
    }
  }

  private void serializeUserNote(List<UserNote> userNotes, JsonGenerator jsonGenerator) throws IOException {
    if (userNotes != null && !userNotes.isEmpty()) {
      jsonGenerator.writeArrayFieldStart("userNotes");
      for (UserNote userNote : userNotes) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("username", userNote.getUsername());
        jsonGenerator.writeStringField("createdDate", userNote.getCreatedDate().toString());
        jsonGenerator.writeStringField("text", userNote.getText());
        jsonGenerator.writeEndObject();
      }
      jsonGenerator.writeEndArray();
    }
  }
}
