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

package org.aktin.broker.manager.persistence.filesystem.models;

import java.time.Instant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.models.NodeStatus;
import org.aktin.broker.xml.RequestStatusInfo;

@XmlRootElement(name = "nodeStatus")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemNodeStatus implements NodeStatus {

  String statusMessage;

  @XmlElement(namespace = org.aktin.broker.xml.XMLConstants.XML_NAMESPACE)
  RequestStatusInfo statusInfo;

  public int getNode() {
    return statusInfo.node;
  }

  public void setNode(int id) {
    statusInfo.node = id;
  }

  public Instant getDeleted() {
    return statusInfo.deleted;
  }

  public void setDeleted(Instant deleted) {
    statusInfo.deleted = deleted;
  }

  public Instant getRetrieved() {
    return statusInfo.retrieved;
  }

  public void setRetrieved(Instant retrieved) {
    statusInfo.retrieved = retrieved;
  }

  public Instant getQueued() {
    return statusInfo.queued;
  }

  public void setQueued(Instant queued) {
    statusInfo.queued = queued;
  }

  public Instant getProcessing() {
    return statusInfo.processing;
  }

  public void setProcessing(Instant processing) {
    statusInfo.processing = processing;
  }

  public Instant getCompleted() {
    return statusInfo.completed;
  }

  public void setCompleted(Instant completed) {
    statusInfo.completed = completed;
  }

  public Instant getRejected() {
    return statusInfo.rejected;
  }

  public void setRejected(Instant rejected) {
    statusInfo.rejected = rejected;
  }

  public Instant getFailed() {
    return statusInfo.failed;
  }

  public void setFailed(Instant failed) {
    statusInfo.failed = failed;
  }

  public Instant getExpired() {
    return statusInfo.expired;
  }

  public void setExpired(Instant expired) {
    statusInfo.expired = expired;
  }
}
