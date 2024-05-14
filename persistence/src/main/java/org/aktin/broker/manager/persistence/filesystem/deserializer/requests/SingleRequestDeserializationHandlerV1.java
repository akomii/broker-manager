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

package org.aktin.broker.manager.persistence.filesystem.deserializer.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.aktin.broker.manager.persistence.filesystem.deserializer.MigrationHandler;
import org.aktin.broker.manager.persistence.filesystem.models.SingleRequestImpl;
import org.aktin.broker.query.xml.QuerySchedule;
import org.aktin.broker.query.xml.SingleExecution;

class SingleRequestDeserializationHandlerV1 extends AbstractRequestDeserializationHandlerV1<SingleRequestImpl> {

  public SingleRequestDeserializationHandlerV1(MigrationHandler<SingleRequestImpl> migrationHandlerChain) {
    super(1, migrationHandlerChain);
  }

  @Override
  protected SingleRequestImpl createRequestInstance() {
    return new SingleRequestImpl();
  }

  @Override
  protected SingleRequestImpl doSerialization(JsonNode node) {
    SingleRequestImpl request = super.doSerialization(node);
    request.setDataVersion(getVersion());
    return request;
  }

  @Override
  protected QuerySchedule deserializeQuerySchedule(JsonNode node) {
    JsonNode scheduleNode = node.get("schedule");
    SingleExecution execution = new SingleExecution();
    execution.duration = deserializePeriod(scheduleNode, "duration");
    return execution;
  }
}
