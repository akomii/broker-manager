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

package org.aktin.broker.manager.service.api.handlers;

import java.io.IOException;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.service.api.exceptions.BrokerException;
import org.aktin.broker.manager.service.api.exceptions.EntityNotFoundException;

public interface RequestExecutionHandler {

  ManagerRequest addNewExecutionToRequest(ManagerRequest request, RequestExecution execution, String creator);

/* TODO?
  void loadAndAddNewExecutionToRequest(int requestId, RequestExecution execution) throws EntityNotFoundException;

  ManagerRequest updateExecutionInRequest(ManagerRequest request, RequestExecution execution);

  void loadAndUpdateExecutionInRequest(int requestId, RequestExecution execution) throws EntityNotFoundException;

  ManagerRequest deleteExecutionFromRequest(ManagerRequest request, int sequenceId);

  void loadAndDeleteExecutionFromRequest(int requestId, int sequenceId) throws EntityNotFoundException;
 */

  ManagerRequest publishExecution(ManagerRequest request, int sequenceId) throws EntityNotFoundException, BrokerException;

  void loadAndPublishExecution(int requestId, int sequenceId) throws EntityNotFoundException, BrokerException, IOException;

  ManagerRequest closeExecution(ManagerRequest request, int sequenceId) throws EntityNotFoundException, BrokerException;

  void loadAndCloseExecution(int requestId, int sequenceId) throws EntityNotFoundException, BrokerException, IOException;

  ManagerRequest archiveExecution(ManagerRequest request, int sequenceId) throws EntityNotFoundException, BrokerException, IOException;

  void loadAndArchiveExecution(int requestId, int sequenceId) throws EntityNotFoundException, BrokerException, IOException;

  /* TODO?
  RequestExecution saveNodeStatusInfoToExecution(RequestExecution execution, NodeStatusInfo statusInfo);
   */
}
