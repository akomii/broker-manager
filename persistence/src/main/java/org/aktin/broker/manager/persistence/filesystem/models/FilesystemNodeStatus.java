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

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.models.NodeStatus;
import org.aktin.broker.xml.RequestStatusInfo;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemNodeStatus extends RequestStatusInfo implements NodeStatus {

  String statusMessage;

  @Min(value = 1, message = "Node ID must be 1 or higher")
  public int getNode() {
    return node;
  }

  public void setNode(int id) {
    node = id;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getDeleted() {
    return deleted;
  }

  public void setDeleted(Instant deleted) {
    this.deleted = deleted;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getRetrieved() {
    return retrieved;
  }

  public void setRetrieved(Instant retrieved) {
    this.retrieved = retrieved;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getQueued() {
    return queued;
  }

  public void setQueued(Instant queued) {
    this.queued = queued;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getProcessing() {
    return processing;
  }

  public void setProcessing(Instant processing) {
    this.processing = processing;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getCompleted() {
    return completed;
  }

  public void setCompleted(Instant completed) {
    this.completed = completed;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getRejected() {
    return rejected;
  }

  public void setRejected(Instant rejected) {
    this.rejected = rejected;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getFailed() {
    return failed;
  }

  public void setFailed(Instant failed) {
    this.failed = failed;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getExpired() {
    return expired;
  }

  public void setExpired(Instant expired) {
    this.expired = expired;
  }
}
