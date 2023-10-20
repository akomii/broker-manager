package org.aktin.broker.manager.api.models;

import java.time.Instant;
import java.util.List;
import org.aktin.broker.manager.api.enums.RequestExecutionState;

public interface RequestExecution {

  Integer getExternalId();

  void setExternalId(Integer externalId);

  Instant getReferenceDate();

  void setReferenceDate(Instant referenceDate);

  Instant getScheduledExecutionDate();

  void setScheduledExecutionDate(Instant scheduledExecutionDate);

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

  //TODO resultsDownloadLog

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  RequestExecutionState getState();

  void setState(RequestExecutionState state);

  List<NodeStatusInfo> getNodeStatusInfos();

  void setNodeStatusInfos(List<NodeStatusInfo> nodeStatusInfos);
}
