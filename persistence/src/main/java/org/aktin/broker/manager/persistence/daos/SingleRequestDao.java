package org.aktin.broker.manager.persistence.daos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.models.SingleRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.SingleExecution;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SingleRequestDao extends AbstractManagerRequest<SingleExecution> implements
    SingleRequest {

  @Override
  public void setQuery(Query query) {
    if (!(query.schedule instanceof SingleExecution)) {
      throw new IllegalArgumentException("Expected a Query with a SingleExecution schedule");
    }
    super.setQuery(query);
  }

  public SingleExecution getQuerySchedule() {
    return (SingleExecution) query.schedule;
  }

  public void setQuerySchedule(SingleExecution schedule) {
    query.schedule = schedule;
  }
}