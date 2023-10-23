package org.aktin.broker.manager.persistence.daos;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.xml.RequestStatusInfo;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NodeStatusInfoDao extends RequestStatusInfo implements NodeStatusInfo {

  Integer executionId;

  String statusMessage;

  public Integer getNode() {
    return node;
  }

  public void setNode(Integer id) {
    node = id;
  }

  public Instant getDeleted() {
    return deleted;
  }

  public void setDeleted(Instant deleted) {
    this.deleted = deleted;
  }

  public Instant getRetrieved() {
    return retrieved;
  }

  public void setRetrieved(Instant retrieved) {
    this.retrieved = retrieved;
  }

  public Instant getQueued() {
    return queued;
  }

  public void setQueued(Instant queued) {
    this.queued = queued;
  }

  public Instant getProcessing() {
    return processing;
  }

  public void setProcessing(Instant processing) {
    this.processing = processing;
  }

  public Instant getCompleted() {
    return completed;
  }

  public void setCompleted(Instant completed) {
    this.completed = completed;
  }

  public Instant getRejected() {
    return rejected;
  }

  public void setRejected(Instant rejected) {
    this.rejected = rejected;
  }

  public Instant getFailed() {
    return failed;
  }

  public void setFailed(Instant failed) {
    this.failed = failed;
  }

  public Instant getExpired() {
    return expired;
  }

  public void setExpired(Instant expired) {
    this.expired = expired;
  }
}
