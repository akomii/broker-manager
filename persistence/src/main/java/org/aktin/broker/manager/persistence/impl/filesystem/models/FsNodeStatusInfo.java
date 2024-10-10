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

package org.aktin.broker.manager.persistence.impl.filesystem.models;

import java.time.Instant;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.models.NodeStatusInfo;
import org.aktin.broker.xml.RequestStatusInfo;

@XmlRootElement(name = "nodeStatusInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsNodeStatusInfo implements NodeStatusInfo {

  String statusMessage;

  @XmlElement(namespace = org.aktin.broker.xml.XMLConstants.XML_NAMESPACE)
  RequestStatusInfo statusInfo;

  @Setter
  @XmlTransient
  transient FsRequestExecution parentExecutionRef;

  @Override
  public String getStatusMessage() {
    return statusMessage;
  }

  @Override
  public void setStatusMessage(String statusMessage) {
    if (!Objects.equals(this.statusMessage, statusMessage)) {
      this.statusMessage = statusMessage;
      markDirty();
    }
  }

  @Override
  public int getNode() {
    return statusInfo != null ? statusInfo.node : 0;
  }

  @Override
  public void setNode(int id) {
    initStatusInfoIfNull();
    if (statusInfo.node != id) {
      statusInfo.node = id;
      markDirty();
    }
  }

  @Override
  public Instant getDeleted() {
    return statusInfo != null ? statusInfo.deleted : null;
  }

  @Override
  public void setDeleted(Instant deleted) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.deleted, deleted)) {
      statusInfo.deleted = deleted;
      markDirty();
    }
  }

  @Override
  public Instant getRetrieved() {
    return statusInfo != null ? statusInfo.retrieved : null;
  }

  @Override
  public void setRetrieved(Instant retrieved) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.retrieved, retrieved)) {
      statusInfo.retrieved = retrieved;
      markDirty();
    }
  }

  @Override
  public Instant getQueued() {
    return statusInfo != null ? statusInfo.queued : null;
  }

  @Override
  public void setQueued(Instant queued) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.queued, queued)) {
      statusInfo.queued = queued;
      markDirty();
    }
  }

  @Override
  public Instant getProcessing() {
    return statusInfo != null ? statusInfo.processing : null;
  }

  @Override
  public void setProcessing(Instant processing) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.processing, processing)) {
      statusInfo.processing = processing;
      markDirty();
    }
  }

  @Override
  public Instant getCompleted() {
    return statusInfo != null ? statusInfo.completed : null;
  }

  @Override
  public void setCompleted(Instant completed) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.completed, completed)) {
      statusInfo.completed = completed;
      markDirty();
    }
  }

  @Override
  public Instant getRejected() {
    return statusInfo != null ? statusInfo.rejected : null;
  }

  @Override
  public void setRejected(Instant rejected) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.rejected, rejected)) {
      statusInfo.rejected = rejected;
      markDirty();
    }
  }

  @Override
  public Instant getFailed() {
    return statusInfo != null ? statusInfo.failed : null;
  }

  @Override
  public void setFailed(Instant failed) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.failed, failed)) {
      statusInfo.failed = failed;
      markDirty();
    }
  }

  @Override
  public Instant getExpired() {
    return statusInfo != null ? statusInfo.expired : null;
  }

  @Override
  public void setExpired(Instant expired) {
    initStatusInfoIfNull();
    if (!Objects.equals(statusInfo.expired, expired)) {
      statusInfo.expired = expired;
      markDirty();
    }
  }

  private void initStatusInfoIfNull() {
    if (statusInfo == null) {
      statusInfo = new RequestStatusInfo();
    }
  }

  private void markDirty() {
    if (parentExecutionRef != null) {
      parentExecutionRef.markDirty();
    }
  }
}
