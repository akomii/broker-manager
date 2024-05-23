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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
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
public class FilesystemNodeStatus extends RequestStatusInfo implements NodeStatus {

  String statusMessage;

  public int getNode() {
    return node;
  }

  public void setNode(int id) {
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
