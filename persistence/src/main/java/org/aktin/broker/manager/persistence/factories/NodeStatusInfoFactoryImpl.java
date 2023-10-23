package org.aktin.broker.manager.persistence.factories;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Instant;
import org.aktin.broker.manager.api.factories.NodeStatusInfoFactory;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.manager.persistence.daos.NodeStatusInfoDao;
import org.springframework.stereotype.Component;

@Component
public class NodeStatusInfoFactoryImpl implements NodeStatusInfoFactory {

  @Override
  public NodeStatusInfo create() {
    return new NodeStatusInfoDao();
  }

  public NodeStatusInfo create(
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
      @Nullable Instant expired) {
    NodeStatusInfoDao dao = new NodeStatusInfoDao();
    dao.setId(id);
    dao.setExecutionId(executionId);
    dao.setStatusMessage(statusMessage);
    dao.setNodeId(node);
    dao.setDeleted(deleted);
    dao.setRetrieved(retrieved);
    dao.setQueued(queued);
    dao.setProcessing(processing);
    dao.setCompleted(completed);
    dao.setRejected(rejected);
    dao.setFailed(failed);
    dao.setExpired(expired);
    return dao;
  }
}
