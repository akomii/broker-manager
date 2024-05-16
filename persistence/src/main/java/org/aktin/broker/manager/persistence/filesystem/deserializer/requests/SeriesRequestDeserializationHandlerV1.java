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
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemSeriesRequest;
import org.aktin.broker.query.xml.QuerySchedule;
import org.aktin.broker.query.xml.RepeatedExecution;

class SeriesRequestDeserializationHandlerV1 extends RequestDeserializationHandlerV1<FilesystemSeriesRequest> {

  public SeriesRequestDeserializationHandlerV1(MigrationHandler<FilesystemSeriesRequest> migrationHandlerChain) {
    super(1, migrationHandlerChain);
  }

  @Override
  protected FilesystemSeriesRequest createRequestInstance() {
    return new FilesystemSeriesRequest();
  }

  @Override
  protected FilesystemSeriesRequest doSerialization(JsonNode node) {
    FilesystemSeriesRequest request = super.doSerialization(node);
    request.setDataVersion(getVersion());
    request.setAnchoredSequenceIdRef(deserializeNumber(node, "anchoredSequenceIdRef"));
    request.setAutoPublishing(deserializeBool(node, "isAutoPublishing"));
    request.setSeriesClosingDate(deserializeDate(node, "seriesClosingDate"));
    request.setSeriesArchiveDate(deserializeDate(node, "seriesArchiveDate"));
    return request;
  }

  @Override
  protected QuerySchedule deserializeQuerySchedule(JsonNode node) {
    JsonNode scheduleNode = node.get("schedule");
    RepeatedExecution execution = new RepeatedExecution();
    execution.duration = deserializePeriod(scheduleNode, "duration");
    execution.interval = deserializePeriod(scheduleNode, "interval");
    execution.intervalHours = deserializeNumber(scheduleNode, "intervalHours");
    execution.id = deserializeNumber(scheduleNode, "id");
    return execution;
  }
}
