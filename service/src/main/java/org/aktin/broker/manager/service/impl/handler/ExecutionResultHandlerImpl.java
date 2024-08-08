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
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.aktin.broker.client.ResponseWithMetadata;
import org.aktin.broker.manager.model.api.factories.DownloadEventFactory;
import org.aktin.broker.manager.model.api.models.DownloadEvent;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.api.exceptions.DataDeleteException;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.service.api.conn.BrokerAdminWrapper;
import org.aktin.broker.manager.service.api.exceptions.BrokerException;
import org.aktin.broker.manager.service.api.exceptions.EntityNotFoundException;
import org.aktin.broker.manager.service.api.exceptions.HashGenerationException;
import org.aktin.broker.manager.service.api.handlers.ExecutionResultHandler;
import org.aktin.broker.manager.service.impl.util.HashGenerator;
import org.aktin.broker.manager.service.impl.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionResultHandlerImpl implements ExecutionResultHandler {

  private static final Logger log = LoggerFactory.getLogger(ExecutionResultHandlerImpl.class);

  private final BrokerAdminWrapper brokerAdmin;
  private final ExecutionResultRepository resultRepository;
  private final ManagerRequestRepository requestRepository;
  private final DownloadEventFactory downloadEventFactory;
  private final HashGenerator hashGenerator;

  public ExecutionResultHandlerImpl(
      BrokerAdminWrapper brokerAdmin,
      ExecutionResultRepository resultRepository,
      ManagerRequestRepository requestRepository,
      DownloadEventFactory downloadEventFactory,
      HashGenerator hashGenerator) {
    this.brokerAdmin = brokerAdmin;
    this.resultRepository = resultRepository;
    this.requestRepository = requestRepository;
    this.downloadEventFactory = downloadEventFactory;
    this.hashGenerator = hashGenerator;
  }

  @Override
  public InputStream addResultFromBrokerServer(int requestId, int sequenceId, String username, Set<String> userOrgs)
      throws EntityNotFoundException, HashGenerationException, BrokerException, IOException {
    // get result export from broker
    log.info("Downloading new results for requestId={} sequenceId={} from broker", requestId, sequenceId);
    ManagerRequest request = RequestUtils.getRequestFromRepository(requestRepository, requestId);
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    int externalId = execution.getExternalId();
    InputStream resultStream = getResultStreamFromBroker(externalId);
    // store result export locally
    int nextIteration = getNextIterationOfResultDownload(execution);
    String identifier = generateResultIdentifier(requestId, sequenceId, nextIteration);
    resultRepository.save(resultStream, identifier);
    // log result download
    DownloadEvent downloadEvent = createNewDownloadEvent(username, userOrgs, resultStream, identifier);
    addDownloadEventToExecution(execution, downloadEvent);
    requestRepository.save(request);
    return resultStream;
  }

  private InputStream getResultStreamFromBroker(int externalId) throws BrokerException, IOException {
    ResponseWithMetadata response = brokerAdmin.getExecutionResult(externalId);
    return response.getInputStream();
  }

  private int getNextIterationOfResultDownload(RequestExecution execution) {
    List<DownloadEvent> downloadEvents = execution.getDownloadEvents();
    Set<String> identifiers = getUniqueResultIdentifiers(downloadEvents);
    return identifiers.size() + 1;
  }

  private Set<String> getUniqueResultIdentifiers(List<DownloadEvent> downloadEvents) {
    return downloadEvents.stream()
        .map(DownloadEvent::getIdentifier)
        .collect(Collectors.toSet());
  }

  // NOTE: We assume the results from broker are always ZIP
  private String generateResultIdentifier(int requestId, int sequenceId, int iteration) {
    return String.format("aktin-export_req%d-seq%d-iter%d.zip", requestId, sequenceId, iteration);
  }

  private DownloadEvent createNewDownloadEvent(String username, Set<String> userOrgs, InputStream resultStream, String identifier)
      throws HashGenerationException {
    DownloadEvent downloadEvent = downloadEventFactory.create();
    downloadEvent.setUsername(username);
    downloadEvent.setUserOrganizations(userOrgs);
    downloadEvent.setDownloadDate(Instant.now());
    String fileHash = hashGenerator.generateHash(resultStream);
    downloadEvent.setDownloadHash(fileHash);
    downloadEvent.setHashAlgorithm(hashGenerator.getAlgorithm());
    downloadEvent.setIdentifier(identifier);
    return downloadEvent;
  }

  private void addDownloadEventToExecution(RequestExecution execution, DownloadEvent downloadEvent) {
    List<DownloadEvent> downloadEvents = execution.getDownloadEvents();
    downloadEvents.add(downloadEvent);
  }

  @Override
  public InputStream getStoredResult(int requestId, int sequenceId, String identifier, String username, Set<String> userOrgs)
      throws EntityNotFoundException, HashGenerationException, IOException {
    // get stored result export
    log.info("Retrieving existing results of {}", identifier);
    InputStream resultStream = getResultStreamFromRepository(identifier);
    // log result download
    ManagerRequest request = RequestUtils.getRequestFromRepository(requestRepository, requestId);
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    DownloadEvent downloadEvent = createNewDownloadEvent(username, userOrgs, resultStream, identifier);
    addDownloadEventToExecution(execution, downloadEvent);
    requestRepository.save(request);
    return resultStream;
  }

  private InputStream getResultStreamFromRepository(String identifier) throws DataReadException, EntityNotFoundException {
    return resultRepository.get(identifier)
        .orElseThrow(() -> new EntityNotFoundException("Result not found: " + identifier));
  }

  @Override
  public void deleteStoredResults(int requestId, int sequenceId) throws EntityNotFoundException, IOException {
    log.info("Deleting all execution results for requestId={} sequenceId={}", requestId, sequenceId);
    ManagerRequest request = RequestUtils.getRequestFromRepository(requestRepository, requestId);
    RequestExecution execution = RequestUtils.getExecutionFromRequest(request, sequenceId);
    deleteDownloadedResults(execution);
  }

  private void deleteDownloadedResults(RequestExecution execution) throws DataDeleteException {
    List<DownloadEvent> downloadEvents = execution.getDownloadEvents();
    if (downloadEvents.isEmpty()) {
      log.info("Request has no downloaded results");
      return;
    }
    Set<String> identifiers = getUniqueResultIdentifiers(downloadEvents);
    for (String identifier : identifiers) {
      resultRepository.delete(identifier);
    }
  }
}
