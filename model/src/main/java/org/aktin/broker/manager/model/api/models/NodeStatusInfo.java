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

package org.aktin.broker.manager.model.api.models;

import java.time.Instant;

/**
 * Represents the status of a {@link RequestExecution} on a specific {@link ManagerNode}. It tracks the progress of the execution through various
 * stages.
 *
 * <p>Status information includes:</p>
 * <ul>
 *   <li><strong>Node ID</strong>: The unique identifier of the node.</li>
 *   <li><strong>Status Messages</strong>: Descriptive messages about the current status.</li>
 *   <li><strong>Timestamps</strong>: Key timestamps marking different stages of the execution lifecycle such as
 *   <ul>
 *   <li><code>Retrieved</code>: Execution was retrieved by the node.</li>
 *   <li><code>Queued</code>: Execution was accepted by the node and put in queue for processing.</li>
 *   <li><code>Processing</code>: Execution is currently processing.</li>
 *   <li><code>Completed</code>: Execution finished successfully and results have been transmitted.</li>
 *   <li><code>Rejected</code>: Execution was rejected by the node.</li>
 *   <li><code>Failed</code>: Execution encountered an error during processing.</li>
 *   <li><code>Expired</code>: Execution exceeded its time limit (due to waiting in queue or waiting to be put in queue) and cannot be processed anymore.</li>
 *   <li><code>Deleted</code>: Execution was deleted from the node.</li>
 *   </ul>
 *   </li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface NodeStatusInfo {

  int getNode();

  void setNode(int id);

  String getStatusMessage();

  void setStatusMessage(String statusMessage);

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
}
