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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.enums.ExecutionState;
import org.aktin.broker.manager.models.DownloadEvent;
import org.aktin.broker.manager.models.NodeStatusInfo;
import org.aktin.broker.manager.models.RequestExecution;

@XmlRootElement(name = "requestExecution")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsRequestExecution implements RequestExecution {

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
  ExecutionState executionState;

  @XmlElementWrapper(name = "nodeStatusInfos")
  @XmlElement(name = "nodeStatusInfo")
  List<NodeStatusInfo> nodeStatusInfos;

  @XmlElementWrapper(name = "downloadEvents")
  @XmlElement(name = "downloadEvent")
  List<DownloadEvent> downloadEvents;
}
