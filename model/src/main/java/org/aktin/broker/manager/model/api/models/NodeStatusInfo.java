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
 * Represents the status of a {@link RequestExecution} on a specific {@link ManagerNode}. Tracks the progress of the {@link RequestExecution} through
 * various stages:
 * <ul>
 *   <li><code>Retrieved</code>: Execution was retrieved by the node.</li>
 *   <li><code>Queued</code>: Execution was accepted by the node and put in queue for processing.</li>
 *   <li><code>Processing</code>: Execution is currently processing.</li>
 *   <li><code>Completed</code>: Execution finished successfully and results have been transmitted.</li>
 *   <li><code>Rejected</code>: Execution was rejected by the node.</li>
 *   <li><code>Failed</code>: Execution encountered an error during processing.</li>
 *   <li><code>Expired</code>: Execution exceeded its time limit (due to waiting in queue or waiting to be put in queue) and cannot be processed anymore.</li>
 *   <li><code>Deleted</code>: Execution was deleted from the node.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface NodeStatusInfo {

  /**
   * Gets the unique identifier of the node associated with this status.
   *
   * @return node ID
   */
  int getNode();

  /**
   * Sets the unique identifier of the node associated with this status.
   *
   * @param id the node ID
   */
  void setNode(int id);

  /**
   * Gets a descriptive message about the current status.
   *
   * @return status message or null if non-existent
   */
  String getStatusMessage();

  /**
   * Sets a descriptive message about the current status.
   *
   * @param statusMessage the status message
   */
  void setStatusMessage(String statusMessage);

  /**
   * Gets the timestamp when the execution was deleted from the node.
   *
   * @return deletion timestamp or null if not deleted
   */
  Instant getDeleted();

  /**
   * Sets the timestamp when the execution was deleted from the node.
   *
   * @param deleted the deletion timestamp
   */
  void setDeleted(Instant deleted);

  /**
   * Gets the timestamp when the execution was retrieved by the node.
   *
   * @return retrieval timestamp or null if not retrieved yet
   */
  Instant getRetrieved();

  /**
   * Sets the timestamp when the execution was retrieved by the node.
   *
   * @param retrieved the retrieval timestamp
   */
  void setRetrieved(Instant retrieved);

  /**
   * Gets the timestamp when the execution was put in queue for processing.
   *
   * @return queueing timestamp or null if not yet queued
   */
  Instant getQueued();

  /**
   * Sets the timestamp when the execution was put in queue for processing.
   *
   * @param queued the queueing timestamp
   */
  void setQueued(Instant queued);

  /**
   * Gets the timestamp when the execution started processing.
   *
   * @return processing timestamp or null if not started
   */
  Instant getProcessing();

  /**
   * Sets the timestamp when the execution started processing.
   *
   * @param processing the processing timestamp
   */
  void setProcessing(Instant processing);

  /**
   * Gets the timestamp when the execution was completed by the node and results have been transmitted.
   *
   * @return completion timestamp or null if not completed
   */
  Instant getCompleted();

  /**
   * Sets the timestamp when the execution was completed by the node and results have been transmitted.
   *
   * @param completed the completion timestamp
   */
  void setCompleted(Instant completed);

  /**
   * Gets the timestamp when the execution was rejected by the node.
   *
   * @return rejection timestamp or null if not rejected
   */
  Instant getRejected();

  /**
   * Sets the timestamp when the execution was rejected by the node.
   *
   * @param rejected the rejection timestamp
   */
  void setRejected(Instant rejected);

  /**
   * Gets the timestamp when the execution failed during processing.
   *
   * @return failure timestamp or null if not failed
   */
  Instant getFailed();

  /**
   * Sets the timestamp when the execution failed during processing.
   *
   * @param failed the failure timestamp
   */
  void setFailed(Instant failed);

  /**
   * Gets the timestamp when the execution expired.
   *
   * @return expiration timestamp or null if not expired
   */
  Instant getExpired();

  /**
   * Sets the timestamp when the execution expired.
   *
   * @param expired the expiration timestamp
   */
  void setExpired(Instant expired);
}
