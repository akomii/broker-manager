package org.aktin.broker.manager.api.factories;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Instant;
import org.aktin.broker.manager.api.models.NodeStatusInfo;

public interface NodeStatusInfoFactory extends GenericFactory<NodeStatusInfo> {

  NodeStatusInfo create(
      @Nonnull Integer id,
      @Nonnull Integer executionId,
      @Nullable String statusMessage,
      @Nonnull Integer node,
      @Nullable Instant deleted,
      @Nullable Instant retrieved,
      @Nullable Instant queued,
      @Nullable Instant processing,
      @Nullable Instant completed,
      @Nullable Instant rejected,
      @Nullable Instant failed,
      @Nullable Instant expired);
}
