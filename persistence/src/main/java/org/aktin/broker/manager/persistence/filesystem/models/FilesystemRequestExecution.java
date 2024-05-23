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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.enums.RequestExecutionState;
import org.aktin.broker.manager.persistence.api.models.DownloadEvent;
import org.aktin.broker.manager.persistence.api.models.NodeStatus;
import org.aktin.broker.manager.persistence.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.filesystem.adapters.DownloadEventAdapter;
import org.aktin.broker.manager.persistence.filesystem.adapters.InstantAdapter;
import org.aktin.broker.manager.persistence.filesystem.adapters.NodeStatusAdapter;

@XmlRootElement(name = "requestExecution")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemRequestExecution implements RequestExecution {

  int sequenceId;

  int externalId;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant referenceDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant executionDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant scheduledPublishDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant publishedDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant scheduledClosingDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant closedDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant scheduledArchiveDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant archivedDate;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant createdDate;

  String createdBy;

  RequestExecutionState executionState;

  @XmlElementWrapper(name = "nodeStatuses")
  @XmlElement(name = "nodeStatus")
  @XmlJavaTypeAdapter(NodeStatusAdapter.class)
  List<NodeStatus> nodeStatuses;

  @XmlElementWrapper(name = "downloadEvents")
  @XmlElement(name = "downloadEvent")
  @XmlJavaTypeAdapter(DownloadEventAdapter.class)
  List<DownloadEvent> downloadEvents;
}
