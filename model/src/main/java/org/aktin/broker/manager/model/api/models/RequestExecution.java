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
import java.util.List;
import org.aktin.broker.manager.model.api.enums.ExecutionState;

/**
 * Represents a single execution instance of a {@link ManagerRequest} within the AKTIN research infrastructure.
 *
 * <p>An execution tracks as:</p>
 * <ul>
 *     <li><strong>Timestamps</strong>: Key moments in the execution process (creation, scheduling, publishing, closing, archiving).</li>
 *     <li><strong>State</strong>: The current status of the execution ({@link ExecutionState}).</li>
 *     <li><strong>Node Statuses</strong>: Information about the execution status on each participating {@link ManagerNode} ({@link NodeStatusInfo}).</li>
 *     <li><strong>Download Events</strong>: Records of download activities associated with the execution results ({@link DownloadEvent}).</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface RequestExecution {

  int getSequenceId();

  void setSequenceId(int sequenceId);

  int getExternalId();

  void setExternalId(int externalId);

  Instant getReferenceDate();

  void setReferenceDate(Instant referenceDate);

  Instant getExecutionDate();

  void setExecutionDate(Instant executionDate);

  Instant getScheduledPublishDate();

  void setScheduledPublishDate(Instant scheduledPublishDate);

  Instant getPublishedDate();

  void setPublishedDate(Instant publishedDate);

  Instant getScheduledClosingDate();

  void setScheduledClosingDate(Instant scheduledClosingDate);

  Instant getClosedDate();

  void setClosedDate(Instant closedDate);

  Instant getScheduledArchiveDate();

  void setScheduledArchiveDate(Instant scheduledArchiveDate);

  Instant getArchivedDate();

  void setArchivedDate(Instant archivedDate);

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getCreatedBy();

  void setCreatedBy(String createdBy);

  ExecutionState getExecutionState();

  void setExecutionState(ExecutionState executionState);

  List<NodeStatusInfo> getNodeStatusInfos();

  void setNodeStatusInfos(List<NodeStatusInfo> nodeStatusInfos);

  void addNodeStatusInfo(NodeStatusInfo nodeStatusInfo);

  List<DownloadEvent> getDownloadEvents();

  void setDownloadEvents(List<DownloadEvent> downloadEvents);

  void addDownloadEvent(DownloadEvent downloadEvent);
}
