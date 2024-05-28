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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.aktin.broker.manager.persistence.api.models.SingleRequest;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.aktin.broker.query.xml.SingleExecution;

@XmlRootElement(name = "singleRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FsSingleRequest extends AbstractManagerRequest implements SingleRequest {

  @Override
  public void setQuery(Query query) {
    if (!(query.schedule instanceof SingleExecution)) {
      throw new IllegalArgumentException("Expected a Query with a SingleExecution schedule");
    }
    super.setQuery(query);
  }

  public void setQuerySchedule(QuerySchedule schedule) {
    if (!(schedule instanceof SingleExecution)) {
      throw new IllegalArgumentException("Expected a SingleExecution schedule");
    }
    query.schedule = schedule;
  }

  public SingleExecution getQuerySchedule() {
    return (SingleExecution) query.schedule;
  }
}
