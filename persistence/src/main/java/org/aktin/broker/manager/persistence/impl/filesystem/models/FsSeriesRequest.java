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
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.models.SeriesRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.aktin.broker.query.xml.RepeatedExecution;

@XmlRootElement(name = "seriesRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsSeriesRequest extends AbstractManagerRequest implements SeriesRequest {

  int anchoredSequenceIdRef;
  boolean isAutoPublishing;
  Instant seriesClosingDate;
  Instant seriesArchiveDate;

  @Override
  public void setQuery(Query query) {
    if (query.schedule != null && !(query.schedule instanceof RepeatedExecution)) {
      throw new IllegalArgumentException("Expected a Query with a RepeatedExecution schedule");
    }
    if (!Objects.equals(this.query, query)) {
      super.setQuery(query);
    }
  }

  @Override
  public RepeatedExecution getQuerySchedule() {
    return (RepeatedExecution) (query != null ? query.schedule : null);
  }

  @Override
  public void setQuerySchedule(QuerySchedule schedule) {
    if (!(schedule instanceof RepeatedExecution)) {
      throw new IllegalArgumentException("Expected a RepeatedExecution schedule");
    }
    initQueryIfNull();
    if (!Objects.equals(query.schedule, schedule)) {
      query.schedule = schedule;
      markDirty();
    }
  }

  @Override
  public int getAnchoredSequenceIdRef() {
    return anchoredSequenceIdRef;
  }

  @Override
  public void setAnchoredSequenceIdRef(int anchoredSequenceIdRef) {
    if (this.anchoredSequenceIdRef != anchoredSequenceIdRef) {
      this.anchoredSequenceIdRef = anchoredSequenceIdRef;
      markDirty();
    }
  }

  @Override
  public boolean isAutoPublishing() {
    return isAutoPublishing;
  }

  @Override
  public void setAutoPublishing(boolean isAutoPublishing) {
    if (this.isAutoPublishing != isAutoPublishing) {
      this.isAutoPublishing = isAutoPublishing;
      markDirty();
    }
  }

  @Override
  public Instant getSeriesClosingDate() {
    return seriesClosingDate;
  }

  @Override
  public void setSeriesClosingDate(Instant seriesClosingDate) {
    if (!Objects.equals(this.seriesClosingDate, seriesClosingDate)) {
      this.seriesClosingDate = seriesClosingDate;
      markDirty();
    }
  }

  @Override
  public Instant getSeriesArchiveDate() {
    return seriesArchiveDate;
  }

  @Override
  public void setSeriesArchiveDate(Instant seriesArchiveDate) {
    if (!Objects.equals(this.seriesArchiveDate, seriesArchiveDate)) {
      this.seriesArchiveDate = seriesArchiveDate;
      markDirty();
    }
  }
}
