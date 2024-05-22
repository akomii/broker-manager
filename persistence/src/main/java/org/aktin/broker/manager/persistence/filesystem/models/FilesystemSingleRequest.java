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

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.aktin.broker.manager.persistence.api.models.SingleRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.SingleExecution;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FilesystemSingleRequest extends AbstractManagerRequest<SingleExecution> implements SingleRequest {

  @Override
  public void setQuery(Query query) {
    if (!(query.schedule instanceof SingleExecution)) {
      throw new IllegalArgumentException("Expected a Query with a SingleExecution schedule");
    }
    super.setQuery(query);
  }

  @NotNull(message = "SingleExecution is mandatory")
  public SingleExecution getQuerySchedule() {
    return (SingleExecution) query.schedule;
  }

  public void setQuerySchedule(SingleExecution schedule) {
    query.schedule = schedule;
  }
}
