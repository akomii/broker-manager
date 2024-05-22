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

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.models.SeriesRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.RepeatedExecution;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemSeriesRequest extends AbstractManagerRequest<RepeatedExecution> implements SeriesRequest {

  @Min(value = 1, message = "Anchored sequence ID reference must be 1 or higher")
  int anchoredSequenceIdRef;

  boolean isAutoPublishing;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  Instant seriesClosingDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  Instant seriesArchiveDate;

  @Override
  public void setQuery(Query query) {
    if (!(query.schedule instanceof RepeatedExecution)) {
      throw new IllegalArgumentException("Expected a Query with a RepeatedExecution schedule");
    }
    super.setQuery(query);
  }

  @NotNull(message = "RepeatedExecution is mandatory")
  public RepeatedExecution getQuerySchedule() {
    return (RepeatedExecution) query.schedule;
  }

  public void setQuerySchedule(RepeatedExecution schedule) {
    query.schedule = schedule;
  }
}
