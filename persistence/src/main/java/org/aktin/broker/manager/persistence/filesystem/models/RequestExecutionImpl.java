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
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.enums.RequestExecutionState;
import org.aktin.broker.manager.persistence.api.models.ResultsDownloadEvent;
import org.aktin.broker.manager.persistence.api.models.NodeStatusInfo;
import org.aktin.broker.manager.persistence.api.models.RequestExecution;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestExecutionImpl implements RequestExecution {

  int sequenceId;
  int externalId;
  Instant referenceDate;
  Instant executionDate;
  Instant scheduledPublishDate;
  Instant publishedDate;
  Instant scheduledClosingDate;
  Instant closedDate;
  Instant scheduledArchiveDate;
  Instant archivedDate;
  Instant createdDate;
  String createdBy;
  RequestExecutionState state;
  List<NodeStatusInfo> nodeStatusInfos;
  List<ResultsDownloadEvent> resultsDownloadEvents;
}
