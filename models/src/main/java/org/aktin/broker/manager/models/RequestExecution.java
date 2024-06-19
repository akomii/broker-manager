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

package org.aktin.broker.manager.models;

import java.time.Instant;
import java.util.List;
import org.aktin.broker.manager.enums.ExecutionState;

/**
 * Represents a single execution of a {@link ManagerRequest} within the AKTIN research infrastructure. Each execution tracks its lifecycle from
 * creation to archiving, capturing details such as:
 * <ul>
 *     <li><code>Timestamps</code>: Key timestamps throughout the execution process (creation, scheduling, publishing, closing, archiving).</li>
 *     <li><code>State</code>: The current status of the execution (see {@link ExecutionState}).</li>
 *     <li><code>Node Status</code>: Status information for each {@link ManagerNode} involved in the execution (see {@link NodeStatusInfo}).</li>
 *     <li><code>Download Events</code>: A record of download activities associated with the execution results (see {@link DownloadEvent}).</li>
 * </ul>
 * <p>
 * This interface serves as a central repository for all information related to a specific request execution, enabling monitoring, analysis, and auditing of data exchange processes within the AKTIN infrastructure.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface RequestExecution {

  /**
   * Gets the internal sequence identifier ({@link ManagerRequest}-side) for this execution.
   *
   * @return the sequence ID
   */
  int getSequenceId();

  /**
   * Sets the internal sequence identifier ({@link ManagerRequest}-side) for this execution.
   *
   * @param sequenceId the sequence ID to set or 0 if not set
   */
  void setSequenceId(int sequenceId);

  /**
   * Gets the external identifier (of the AKTIN query broker) for this execution.
   *
   * @return the external ID or 0 if not set
   */
  int getExternalId();

  /**
   * Sets the external identifier (of the AKTIN query broker) for this execution.
   *
   * @param externalId the external ID to set
   */
  void setExternalId(int externalId);

  /**
   * Gets the reference date for the data query associated for this request execution.
   *
   * @return the data query reference date
   */
  Instant getReferenceDate();

  /**
   * Sets the reference date for the data query associated for this request execution.
   *
   * @param referenceDate the data query reference date to set
   */
  void setReferenceDate(Instant referenceDate);

  /**
   * Gets the date when the nodes associated with this request execution shall execute the query.
   *
   * @return the node execution date
   */
  Instant getExecutionDate();

  /**
   * Sets the date when the nodes associated with this request execution shall execute the query.
   *
   * @param executionDate the node execution date to set
   */
  void setExecutionDate(Instant executionDate);

  /**
   * Gets the scheduled publishing date for this request execution.
   *
   * @return the scheduled publishing date
   */
  Instant getScheduledPublishDate();

  /**
   * Sets the scheduled publishing date for this request execution.
   *
   * @param scheduledPublishDate the scheduled publishing date to set
   */
  void setScheduledPublishDate(Instant scheduledPublishDate);

  /**
   * Gets the published date of this request execution.
   *
   * @return the published date
   */
  Instant getPublishedDate();

  /**
   * Sets the published date of this request execution.
   *
   * @param publishedDate the published date to set
   */
  void setPublishedDate(Instant publishedDate);

  /**
   * Gets the scheduled closing date for this request execution.
   *
   * @return the scheduled closing date
   */
  Instant getScheduledClosingDate();

  /**
   * Sets the scheduled closing date for this request execution.
   *
   * @param scheduledClosingDate the scheduled closing date to set
   */
  void setScheduledClosingDate(Instant scheduledClosingDate);

  /**
   * Gets the closed date of this request execution.
   *
   * @return the closed date
   */
  Instant getClosedDate();

  /**
   * Sets the closed date of this request execution.
   *
   * @param closedDate the closed date to set
   */
  void setClosedDate(Instant closedDate);

  /**
   * Gets the scheduled archival date for this request execution.
   *
   * @return the scheduled archival date
   */
  Instant getScheduledArchiveDate();

  /**
   * Sets the scheduled archival date for this request execution.
   *
   * @param scheduledArchiveDate the scheduled archival date to set
   */
  void setScheduledArchiveDate(Instant scheduledArchiveDate);

  /**
   * Gets the archival date of this request execution.
   *
   * @return the archival date
   */
  Instant getArchivedDate();

  /**
   * Sets the archival date of this request execution.
   *
   * @param archivedDate the archival date to set
   */
  void setArchivedDate(Instant archivedDate);

  /**
   * Gets the date when this request execution was created.
   *
   * @return the creation date
   */
  Instant getCreatedDate();

  /**
   * Sets the creation date of this request execution.
   *
   * @param createdDate the creation date to set
   */
  void setCreatedDate(Instant createdDate);

  /**
   * Gets the username who created this request execution.
   *
   * @return the username of the creator
   */
  String getCreatedBy();

  /**
   * Sets the username who created this request execution.
   *
   * @param createdBy the username of the creator
   */
  void setCreatedBy(String createdBy);

  /**
   * Gets the current state of the request execution.
   *
   * @return the execution state
   */
  ExecutionState getExecutionState();

  /**
   * Sets the current state of the request execution.
   *
   * @param executionState the execution state to set
   */
  void setExecutionState(ExecutionState executionState);

  /**
   * Gets the list of node status information objects associated with this request execution.
   *
   * @return a list of {@link NodeStatusInfo} objects
   */
  List<NodeStatusInfo> getNodeStatusInfos();

  /**
   * Sets the list of node status information objects associated with this request execution.
   *
   * @param nodeStatusInfos the list of {@link NodeStatusInfo} objects to set
   */
  void setNodeStatusInfos(List<NodeStatusInfo> nodeStatusInfos);

  /**
   * Gets the list of download events associated with this request execution.
   *
   * @return a list of {@link DownloadEvent} objects
   */
  List<DownloadEvent> getDownloadEvents();

  /**
   * Sets the list of download events associated with this request execution.
   *
   * @param downloadEvents the list of {@link DownloadEvent} objects to set
   */
  void setDownloadEvents(List<DownloadEvent> downloadEvents);
}
