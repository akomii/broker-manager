package org.aktin.broker.manager.api.factories;

import java.time.Instant;
import org.aktin.broker.manager.api.models.NodeStatusInfo;

public interface NodeStatusInfoFactory extends GenericFactory<NodeStatusInfo> {

  NodeStatusInfo create(
      Integer node,
      Integer executionId,
      String statusMessage,
      Instant deleted,
      Instant retrieved,
      Instant queued,
      Instant processing,
      Instant completed,
      Instant rejected,
      Instant failed,
      Instant expired);
}
