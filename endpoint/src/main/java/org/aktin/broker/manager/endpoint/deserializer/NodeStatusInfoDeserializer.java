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
    return nodeStatusInfoFactory.create(
        jsonNode.get("id").asInt(),
        jsonNode.get("executionId").asInt(),
        jsonNode.get("statusMessage").asText(),
        jsonNode.get("node").asInt(),
        Instant.parse(jsonNode.get("deleted").asText()),
        Instant.parse(jsonNode.get("retrieved").asText()),
        Instant.parse(jsonNode.get("queued").asText()),
        Instant.parse(jsonNode.get("processing").asText()),
        Instant.parse(jsonNode.get("completed").asText()),
        Instant.parse(jsonNode.get("rejected").asText()),
        Instant.parse(jsonNode.get("failed").asText()),
        Instant.parse(jsonNode.get("expired").asText())
    );
  }
}
