package org.aktin.broker.manager.api.models;

import java.time.Instant;
import org.aktin.broker.xml.RequestStatus;

public interface NodeStatusInfo {

  Integer getExecutionId();

  void setExecutionId(Integer id);

  String getStatusMessage();

  void setStatusMessage(String statusMessage);

  Integer getNode();

  void setNode(Integer id);

  Instant getDeleted();

  void setDeleted(Instant deleted);

  Instant getRetrieved();

  void setRetrieved(Instant retrieved);

  Instant getQueued();

  void setQueued(Instant queued);

  Instant getProcessing();

  void setProcessing(Instant processing);

  Instant getCompleted();

  void setCompleted(Instant completed);

  Instant getRejected();

  void setRejected(Instant rejected);

  Instant getFailed();

  void setFailed(Instant failed);

  Instant getExpired();

  void setExpired(Instant expired);

  RequestStatus getStatus();
}
