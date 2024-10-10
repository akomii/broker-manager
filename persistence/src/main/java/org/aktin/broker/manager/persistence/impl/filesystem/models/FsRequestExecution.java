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

package org.aktin.broker.manager.persistence.impl.filesystem.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.enums.ExecutionState;
import org.aktin.broker.manager.model.api.models.DownloadEvent;
import org.aktin.broker.manager.model.api.models.NodeStatusInfo;
import org.aktin.broker.manager.model.api.models.RequestExecution;

@XmlRootElement(name = "requestExecution")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@NoArgsConstructor
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

  @Setter
  @XmlTransient
  transient AbstractManagerRequest parentRequestRef;


  @Override
  public int getSequenceId() {
    return sequenceId;
  }

  @Override
  public void setSequenceId(int sequenceId) {
    if (this.sequenceId != sequenceId) {
      this.sequenceId = sequenceId;
      markDirty();
    }
  }

  @Override
  public int getExternalId() {
    return externalId;
  }

  @Override
  public void setExternalId(int externalId) {
    if (this.externalId != externalId) {
      this.externalId = externalId;
      markDirty();
    }
  }

  @Override
  public Instant getReferenceDate() {
    return referenceDate;
  }

  @Override
  public void setReferenceDate(Instant referenceDate) {
    if (!Objects.equals(this.referenceDate, referenceDate)) {
      this.referenceDate = referenceDate;
      markDirty();
    }
  }

  @Override
  public Instant getExecutionDate() {
    return executionDate;
  }

  @Override
  public void setExecutionDate(Instant executionDate) {
    if (!Objects.equals(this.executionDate, executionDate)) {
      this.executionDate = executionDate;
      markDirty();
    }
  }

  @Override
  public Instant getScheduledPublishDate() {
    return scheduledPublishDate;
  }

  @Override
  public void setScheduledPublishDate(Instant scheduledPublishDate) {
    if (!Objects.equals(this.scheduledPublishDate, scheduledPublishDate)) {
      this.scheduledPublishDate = scheduledPublishDate;
      markDirty();
    }
  }

  @Override
  public Instant getPublishedDate() {
    return publishedDate;
  }

  @Override
  public void setPublishedDate(Instant publishedDate) {
    if (!Objects.equals(this.publishedDate, publishedDate)) {
      this.publishedDate = publishedDate;
      markDirty();
    }
  }

  @Override
  public Instant getScheduledClosingDate() {
    return scheduledClosingDate;
  }

  @Override
  public void setScheduledClosingDate(Instant scheduledClosingDate) {
    if (!Objects.equals(this.scheduledClosingDate, scheduledClosingDate)) {
      this.scheduledClosingDate = scheduledClosingDate;
      markDirty();
    }
  }

  @Override
  public Instant getClosedDate() {
    return closedDate;
  }

  @Override
  public void setClosedDate(Instant closedDate) {
    if (!Objects.equals(this.closedDate, closedDate)) {
      this.closedDate = closedDate;
      markDirty();
    }
  }

  @Override
  public Instant getScheduledArchiveDate() {
    return scheduledArchiveDate;
  }

  @Override
  public void setScheduledArchiveDate(Instant scheduledArchiveDate) {
    if (!Objects.equals(this.scheduledArchiveDate, scheduledArchiveDate)) {
      this.scheduledArchiveDate = scheduledArchiveDate;
      markDirty();
    }
  }

  @Override
  public Instant getArchivedDate() {
    return archivedDate;
  }

  @Override
  public void setArchivedDate(Instant archivedDate) {
    if (!Objects.equals(this.archivedDate, archivedDate)) {
      this.archivedDate = archivedDate;
      markDirty();
    }
  }

  @Override
  public Instant getCreatedDate() {
    return createdDate;
  }

  @Override
  public void setCreatedDate(Instant createdDate) {
    if (!Objects.equals(this.createdDate, createdDate)) {
      this.createdDate = createdDate;
      markDirty();
    }
  }

  @Override
  public String getCreatedBy() {
    return createdBy;
  }

  @Override
  public void setCreatedBy(String createdBy) {
    if (!Objects.equals(this.createdBy, createdBy)) {
      this.createdBy = createdBy;
      markDirty();
    }
  }

  @Override
  public ExecutionState getExecutionState() {
    return executionState;
  }

  @Override
  public void setExecutionState(ExecutionState executionState) {
    if (this.executionState != executionState) {
      this.executionState = executionState;
      markDirty();
    }
  }

  @Override
  public List<NodeStatusInfo> getNodeStatusInfos() {
    return nodeStatusInfos != null ? Collections.unmodifiableList(nodeStatusInfos) : Collections.emptyList();
  }

  @Override
  public void setNodeStatusInfos(List<NodeStatusInfo> nodeStatusInfos) {
    if (this.nodeStatusInfos == null || !this.nodeStatusInfos.equals(nodeStatusInfos)) {
      if (this.nodeStatusInfos != null) {
        for (NodeStatusInfo info : this.nodeStatusInfos) {
          removeParentRefFromNodeStatusInfo(info);
        }
      }
      this.nodeStatusInfos = nodeStatusInfos;
      if (this.nodeStatusInfos != null) {
        for (NodeStatusInfo info : this.nodeStatusInfos) {
          setParentRefToNodeStatusInfo(info);
        }
      }
      markDirty();
    }
  }

  @Override
  public void addNodeStatusInfo(NodeStatusInfo nodeStatusInfo) {
    if (nodeStatusInfos == null) {
      nodeStatusInfos = new ArrayList<>();
    }
    if (nodeStatusInfo != null) {
      setParentRefToNodeStatusInfo(nodeStatusInfo);
      nodeStatusInfos.add(nodeStatusInfo);
      markDirty();
    }
  }

  @Override
  public void removeNodeStatusInfo(NodeStatusInfo nodeStatusInfo) {
    if (nodeStatusInfos != null && nodeStatusInfos.remove(nodeStatusInfo)) {
      removeParentRefFromNodeStatusInfo(nodeStatusInfo);
      markDirty();
    }
  }

  private void setParentRefToNodeStatusInfo(NodeStatusInfo info) {
    if (info instanceof FsNodeStatusInfo) {
      ((FsNodeStatusInfo) info).setParentExecutionRef(this);
    }
  }

  private void removeParentRefFromNodeStatusInfo(NodeStatusInfo info) {
    if (info instanceof FsNodeStatusInfo) {
      ((FsNodeStatusInfo) info).setParentExecutionRef(null);
    }
  }

  @Override
  public List<DownloadEvent> getDownloadEvents() {
    return downloadEvents != null ? Collections.unmodifiableList(downloadEvents) : Collections.emptyList();
  }

  @Override
  public void setDownloadEvents(List<DownloadEvent> downloadEvents) {
    if (this.downloadEvents == null || !this.downloadEvents.equals(downloadEvents)) {
      this.downloadEvents = downloadEvents;
      markDirty();
    }
  }

  @Override
  public void addDownloadEvent(DownloadEvent downloadEvent) {
    if (downloadEvents == null) {
      downloadEvents = new ArrayList<>();
    }
    if (downloadEvent != null) {
      downloadEvents.add(downloadEvent);
      markDirty();
    }
  }

  protected void markDirty() {
    if (parentRequestRef != null) {
      parentRequestRef.markDirty();
    }
  }
}
