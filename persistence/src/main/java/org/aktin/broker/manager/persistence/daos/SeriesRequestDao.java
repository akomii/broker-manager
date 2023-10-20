package org.aktin.broker.manager.persistence.daos;

import java.time.Instant;
import java.time.Period;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.models.SeriesRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.RepeatedExecution;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeriesRequestDao extends AbstractManagerRequest<RepeatedExecution> implements
    SeriesRequest {

  boolean isAutoPublishing;

  Instant seriesClosingDate;

  Instant seriesArchiveDate;

  @Override
  public void setQuery(Query query) {
    if (!(query.schedule instanceof RepeatedExecution)) {
      throw new IllegalArgumentException("Expected a Query with a RepeatedExecution schedule");
    }
    super.setQuery(query);
  }

  public RepeatedExecution getQuerySchedule() {
    return (RepeatedExecution) query.schedule;
  }

  public void setQuerySchedule(RepeatedExecution schedule) {
    query.schedule = schedule;
  }

  public Period getQueryInterval() {
    return getQuerySchedule().interval;
  }

  public void setQueryInterval(Period interval) {
    getQuerySchedule().interval = interval;
  }

  public int getQueryIntervalHours() {
    return getQuerySchedule().intervalHours;
  }

  public void setQueryIntervalHours(int intervalHours) {
    getQuerySchedule().intervalHours = intervalHours;
  }

  public int getQueryId() {
    return getQuerySchedule().id;
  }

  public void setQueryId(int id) {
    getQuerySchedule().id = id;
  }
}
