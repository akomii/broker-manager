package org.aktin.broker.manager.endpoint.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Instant;
import org.aktin.broker.manager.api.factories.NodeStatusInfoFactory;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeStatusInfoDeserializer extends JsonDeserializer<NodeStatusInfo> {

  @Autowired
  private NodeStatusInfoFactory nodeStatusInfoFactory;

  @Override
  public NodeStatusInfo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode jsonNode = p.getCodec().readTree(p);

    Integer node = jsonNode.has("node") ? jsonNode.get("node").asInt() : null;
    Integer executionId = jsonNode.has("executionId") ? jsonNode.get("executionId").asInt() : null;
    String statusMessage =
        jsonNode.has("statusMessage") ? jsonNode.get("statusMessage").asText() : null;
    Instant deleted = parseInstant(jsonNode, "deleted");
    Instant retrieved = parseInstant(jsonNode, "retrieved");
    Instant queued = parseInstant(jsonNode, "queued");
    Instant processing = parseInstant(jsonNode, "processing");
    Instant completed = parseInstant(jsonNode, "completed");
    Instant rejected = parseInstant(jsonNode, "rejected");
    Instant failed = parseInstant(jsonNode, "failed");
    Instant expired = parseInstant(jsonNode, "expired");
    return nodeStatusInfoFactory.create(
        node,
        executionId,
        statusMessage,
        deleted,
        retrieved,
        queued,
        processing,
        completed,
        rejected,
        failed,
        expired
    );
  }

  private Instant parseInstant(JsonNode jsonNode, String fieldName) {
    if (jsonNode.has(fieldName)) {
      try {
        return Instant.parse(jsonNode.get(fieldName).asText());
      } catch (Exception e) {
        return null; // Return null or some default value
      }
    }
    return null; // Return null for missing field
  }
}
