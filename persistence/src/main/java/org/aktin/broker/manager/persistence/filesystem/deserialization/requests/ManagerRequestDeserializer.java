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

package org.aktin.broker.manager.persistence.filesystem.deserialization.requests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.filesystem.deserialization.DeserializationHandler;
import org.aktin.broker.query.xml.QuerySchedule;

public class ManagerRequestDeserializer extends StdDeserializer<ManagerRequest<? extends QuerySchedule>> {

  private static final Map<Integer, Map<String, DeserializationHandler<? extends ManagerRequest<? extends QuerySchedule>>>> HANDLERS = new HashMap<>();

  static {
    Map<String, DeserializationHandler<? extends ManagerRequest<? extends QuerySchedule>>> v1Handlers = new HashMap<>();
    v1Handlers.put("SingleRequest", new SingleRequestDeserializationV1(null));
    v1Handlers.put("SeriesRequest", new SeriesRequestDeserializationV1(null));
    HANDLERS.put(1, v1Handlers);
  }

  public ManagerRequestDeserializer() {
    this(null);
  }

  protected ManagerRequestDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public ManagerRequest<? extends QuerySchedule> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    if (!node.has("dataVersion")) {
      throw new IllegalArgumentException("dataVersion is required in ManagerRequest");
    }
    int dataVersion = node.get("dataVersion").asInt();
    String requestType = node.get("type").asText();
    Map<String, DeserializationHandler<? extends ManagerRequest<? extends QuerySchedule>>> handlers = HANDLERS.get(dataVersion);
    if (handlers == null) {
      throw new IllegalArgumentException("No handlers found for data version: " + dataVersion);
    }
    DeserializationHandler<? extends ManagerRequest<? extends QuerySchedule>> handler = handlers.get(requestType);
    if (handler == null) {
      throw new IllegalArgumentException("Unknown request type: " + requestType);
    }
    return handler.deserialize(node);
  }
}
