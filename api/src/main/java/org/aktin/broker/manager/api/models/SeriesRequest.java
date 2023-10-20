package org.aktin.broker.manager.api.models;

import java.time.Instant;
import java.time.Period;
import org.aktin.broker.query.xml.RepeatedExecution;

public interface SeriesRequest extends ManagerRequest<RepeatedExecution> {

  boolean isAutoPublishing();

  void setAutoPublishing(boolean autoPublishing);

  Instant getSeriesClosingDate();

  void setSeriesClosingDate(Instant seriesClosingDate);

  Instant getSeriesArchiveDate();

  void setSeriesArchiveDate(Instant seriesArchiveDate);

  Period getQueryInterval();

  void setQueryInterval(Period interval);

  int getQueryIntervalHours();

  void setQueryIntervalHours(int intervalHours);

  int getQueryId();

  void setQueryId(int id);
}
