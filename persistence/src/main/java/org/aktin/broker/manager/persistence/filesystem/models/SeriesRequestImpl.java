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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.models.SeriesRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.RepeatedExecution;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeriesRequestImpl extends AbstractManagerRequest<RepeatedExecution> implements SeriesRequest {

  int anchoredSequenceIdRef;

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
}
