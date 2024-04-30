/*
 *    Copyright (c) 2024  AKTIN
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.api.models;

import java.time.Instant;

/**
 * Represents the status of a {@link RequestExecution} on a specific {@link ManagerNode}. Tracks the progress of the execution through various
 * stages.
 */
public interface NodeStatusInfo {

  int getNode();

  void setNode(int id);

  String getStatusMessage();

  void setStatusMessage(String statusMessage);

  Instant getDeleted();

  void setDeleted(Instant deleted);

  Instant getRetrieved();

  void setRetrieved(Instant retrieved);

  Instant getQueued();

  void setQueued(Instant queued);

  Instant getProcessing();

  void setProcessing(Instant processing);

  Instant getCompleted();

  void setCompleted(Instant completed);

  Instant getRejected();

  void setRejected(Instant rejected);

  Instant getFailed();

  void setFailed(Instant failed);

  Instant getExpired();

  void setExpired(Instant expired);
}
