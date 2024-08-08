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

package org.aktin.broker.manager.service.impl.handler;

import java.io.IOException;
import java.time.Instant;
import org.aktin.broker.manager.model.api.enums.ExecutionState;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.service.api.conn.BrokerAdminWrapper;
import org.aktin.broker.manager.service.api.exceptions.BrokerException;
import org.aktin.broker.manager.service.api.exceptions.EntityNotFoundException;
import org.aktin.broker.manager.service.api.handlers.ExecutionResultHandler;
import org.aktin.broker.manager.service.api.handlers.RequestExecutionHandler;
import org.aktin.broker.manager.service.impl.util.ExecutionStateManager;
import org.aktin.broker.manager.service.impl.util.RequestUtils;
import org.aktin.broker.query.xml.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestExecutionHandlerImpl implements RequestExecutionHandler {

  private static final Logger log = LoggerFactory.getLogger(RequestExecutionHandlerImpl.class);

  private final BrokerAdminWrapper brokerAdmin;
  private final ManagerRequestRepository requestRepository;
  private final ExecutionResultHandler resultHandler;

  public RequestExecutionHandlerImpl(
      BrokerAdminWrapper brokerAdmin,
      ManagerRequestRepository requestRepository,
      ExecutionResultHandler resultHandler) {
    this.brokerAdmin = brokerAdmin;
    this.requestRepository = requestRepository;
    this.resultHandler = resultHandler;
  }

  /*
  referenceDate
  executionDate
  scheduledPublishDate
  scheduledClosingDate
  scheduledArchiveDate
  -> are required but validated earlier?
  sequenceID can be already provided by frontend
   */
  @Override
  public ManagerRequest addNewExecutionToRequest(ManagerRequest request, RequestExecution execution, String creator) {
    execution.setExecutionState(ExecutionState.PENDING);
    if (execution.getSequenceId() == 0) {
      int nextSeqId = request.getRequestExecutions().size() + 1;
      execution.setSequenceId(nextSeqId);
    }
    execution.setCreatedDate(Instant.now());
    execution.setCreatedBy(creator);
    request.getRequestExecutions().add(execution);
    return request;
  }

  @Override
  public ManagerRequest publishExecution(ManagerRequest request, int sequenceId) throws EntityNotFoundException, BrokerException {
    log.info("Publishing execution for requestId={} sequenceId={}", request.getId(), sequenceId);
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    ExecutionStateManager.setToPublished(request.getId(), execution);
    int externalId = brokerAdmin.allocateNewExecutionOnBroker();
    execution.setExternalId(externalId);
    QueryRequest queryRequest = createQueryRequestFromRequest(request, sequenceId);
    int[] targetNodes = request.getTargetNodes().stream().mapToInt(Integer::intValue).toArray();
    brokerAdmin.publishExecutionOnBroker(queryRequest, targetNodes);
    return request;
  }

  // QueryRequest ID can only be acquired from broker
  private QueryRequest createQueryRequestFromRequest(ManagerRequest request, int sequenceId) throws EntityNotFoundException {
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    return new QueryRequest(execution.getExternalId(),
        execution.getReferenceDate(),
        execution.getExecutionDate(),
        execution.getScheduledClosingDate(),
        request.getQuery());
  }

  @Override
  public void loadAndPublishExecution(int requestId, int sequenceId) throws EntityNotFoundException, BrokerException, IOException {
    ManagerRequest request = RequestUtils.getRequestFromRepository(requestRepository, requestId);
    request = publishExecution(request, sequenceId);
    requestRepository.save(request);
  }

  @Override
  public ManagerRequest closeExecution(ManagerRequest request, int sequenceId) throws EntityNotFoundException, BrokerException {
    log.info("Closing execution for requestId={} sequenceId={}", request.getId(), sequenceId);
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    ExecutionStateManager.setToClosed(request.getId(), execution);
    brokerAdmin.closeExecutionOnBroker(execution.getExternalId());
    return request;
  }

  @Override
  public void loadAndCloseExecution(int requestId, int sequenceId) throws EntityNotFoundException, BrokerException, IOException {
    ManagerRequest request = RequestUtils.getRequestFromRepository(requestRepository, requestId);
    request = closeExecution(request, sequenceId);
    requestRepository.save(request);
  }

  @Override
  public ManagerRequest archiveExecution(ManagerRequest request, int sequenceId) throws EntityNotFoundException, BrokerException, IOException {
    log.info("Archiving execution for requestId={} sequenceId={}", request.getId(), sequenceId);
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    ExecutionStateManager.setToArchived(request.getId(), execution);
    brokerAdmin.deleteExecutionFromBroker(execution.getExternalId());
    resultHandler.deleteStoredResults(request.getId(), sequenceId);
    return request;
  }

  @Override
  public void loadAndArchiveExecution(int requestId, int sequenceId) throws EntityNotFoundException, BrokerException, IOException {
    ManagerRequest request = RequestUtils.getRequestFromRepository(requestRepository, requestId);
    request = archiveExecution(request, sequenceId);
    requestRepository.save(request);
  }
}
