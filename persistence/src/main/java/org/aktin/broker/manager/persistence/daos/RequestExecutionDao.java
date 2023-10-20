package org.aktin.broker.manager.persistence.daos;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.enums.RequestExecutionState;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.manager.api.models.RequestExecution;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestExecutionDao implements RequestExecution {

  Integer externalId;

  Instant referenceDate;

  Instant scheduledExecutionDate;

  Instant scheduledPublishDate;

  Instant publishedDate;

  Instant scheduledClosingDate;

  Instant closedDate;

  Instant scheduledArchiveDate;

  Instant archivedDate;

  Instant createdDate;

  RequestExecutionState state;

  List<NodeStatusInfo> nodeStatusInfos;
}
