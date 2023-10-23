package org.aktin.broker.manager.persistence.factories;

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
      Instant expired) {
    NodeStatusInfoDao dao = new NodeStatusInfoDao();
    dao.setNode(node);
    dao.setExecutionId(executionId);
    dao.setStatusMessage(statusMessage);
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
