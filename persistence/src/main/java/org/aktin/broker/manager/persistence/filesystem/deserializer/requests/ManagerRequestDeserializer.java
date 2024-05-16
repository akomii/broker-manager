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

package org.aktin.broker.manager.persistence.filesystem.deserializer.requests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.filesystem.deserializer.DeserializationHandler;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemSeriesRequest;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemSingleRequest;
import org.aktin.broker.query.xml.QuerySchedule;

public class ManagerRequestDeserializer extends StdDeserializer<ManagerRequest<? extends QuerySchedule>> {

  private static final Map<Integer, DeserializationHandler<FilesystemSingleRequest>> SINGLE_HANDLERS = new HashMap<>();
  private static final Map<Integer, DeserializationHandler<FilesystemSeriesRequest>> SERIES_HANDLERS = new HashMap<>();

  static {
    SINGLE_HANDLERS.put(1, new SingleRequestDeserializationHandlerV1(null));
    SERIES_HANDLERS.put(1, new SeriesRequestDeserializationHandlerV1(null));
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
    int dataVersion = node.has("dataVersion") ? node.get("dataVersion").asInt() : 1;
    String requestType = node.get("type").asText();
    DeserializationHandler<? extends ManagerRequest<? extends QuerySchedule>> handler = switch (requestType) {
      case "SingleRequest" -> SINGLE_HANDLERS.get(dataVersion);
      case "SeriesRequest" -> SERIES_HANDLERS.get(dataVersion);
      default -> throw new IllegalArgumentException("Unknown request type: " + requestType);
    };
    if (handler == null) {
      throw new IllegalArgumentException("No handler found for data version: " + dataVersion);
    }
    return handler.deserialize(node);
  }
}
