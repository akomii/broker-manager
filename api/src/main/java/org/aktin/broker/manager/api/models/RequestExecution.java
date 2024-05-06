/*
 *    Copyright (c) 2024  AKTIN
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.api.models;

import java.time.Instant;
import java.util.List;
import org.aktin.broker.manager.api.enums.RequestExecutionState;

/**
 * Represents a single execution of a {@link ManagerRequest}. Captures execution-specific timestamps, state, and associated events (e.g., node
 * statuses, downloads).
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

  RequestExecutionState getState();

  void setState(RequestExecutionState executionState);

  List<NodeStatusInfo> getNodeStatusInfos();

  void setNodeStatusInfos(List<NodeStatusInfo> nodeStatusInfos);

  List<DownloadEvent> getDownloadEvents();

  void setDownloadEvents(List<DownloadEvent> downloadEvents);
}
