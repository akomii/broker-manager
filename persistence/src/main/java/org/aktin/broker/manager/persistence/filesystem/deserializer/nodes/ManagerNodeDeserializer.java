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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.filesystem.deserializer.DeserializationHandler;
import org.aktin.broker.manager.persistence.filesystem.models.ManagerNodeImpl;
import org.springframework.stereotype.Component;

@Component
public class ManagerNodeDeserializer extends StdDeserializer<ManagerNode> {

  private static final Map<Integer, DeserializationHandler<ManagerNodeImpl>> DESERIALIZATION_HANDLER_MAP = new HashMap<>();

  public ManagerNodeDeserializer() {
    this(null);
    DESERIALIZATION_HANDLER_MAP.put(1, new ManagerNodeDeserializationHandlerV1(null));
  }

  protected ManagerNodeDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public ManagerNode deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    int dataVersion = node.has("dataVersion") ? node.get("dataVersion").asInt() : 1;
    DeserializationHandler<ManagerNodeImpl> handler = DESERIALIZATION_HANDLER_MAP.get(dataVersion);
    return handler.deserialize(node);
  }
}
