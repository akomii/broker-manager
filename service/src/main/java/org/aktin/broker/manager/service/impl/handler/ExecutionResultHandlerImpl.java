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
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client.ResponseWithMetadata;
import org.aktin.broker.manager.model.api.factories.DownloadEventFactory;
import org.aktin.broker.manager.model.api.models.DownloadEvent;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.service.api.exceptions.EntityNotFoundException;
import org.aktin.broker.manager.service.api.exceptions.FileHashingException;
import org.aktin.broker.manager.service.api.handlers.ExecutionResultHandler;
import org.aktin.broker.manager.service.impl.util.FileHashGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionResultHandlerImpl implements ExecutionResultHandler {

  private static final Logger log = LoggerFactory.getLogger(ExecutionResultHandlerImpl.class);

  private final BrokerAdmin brokerAdmin;
  private final ExecutionResultRepository resultRepository;
  private final ManagerRequestRepository requestRepository;
  private final DownloadEventFactory downloadEventFactory;
  private final FileHashGenerator fileHashGenerator;

  public ExecutionResultHandlerImpl(
      BrokerAdmin brokerAdmin,
      ExecutionResultRepository resultRepository,
      ManagerRequestRepository requestRepository,
      DownloadEventFactory downloadEventFactory,
      FileHashGenerator fileHashGenerator) {
    this.brokerAdmin = brokerAdmin;
    this.resultRepository = resultRepository;
    this.requestRepository = requestRepository;
    this.downloadEventFactory = downloadEventFactory;
    this.fileHashGenerator = fileHashGenerator;
  }

  // NOTE: We assume the results from broker are always ZIP
  @Override
  public InputStream downloadBrokerServerResults(int requestId, int sequenceId, String username, Set<String> userOrgs)
      throws EntityNotFoundException, FileHashingException, IOException {
    log.info("Downloading new results for requestId={} sequenceId={} from broker", requestId, sequenceId);
    // get result export from broker
    ManagerRequest request = getRequestFromRepository(requestId);
    RequestExecution execution = getExecutionFromRequest(request, sequenceId);
    InputStream resultStream = getResultStreamFromBroker(execution);
    // store result export locally
    int nextIteration = getNextIterationOfFileDownload(execution);
    String filename = generateResultFilename(requestId, sequenceId, nextIteration);
    resultRepository.save(resultStream, filename);
    // log result download
    DownloadEvent downloadEvent = createNewDownloadEvent(username, userOrgs, resultStream, filename);
    saveDownloadEventInRequest(request, sequenceId, downloadEvent);
    return resultStream;
  }

  private ManagerRequest getRequestFromRepository(int requestId) throws EntityNotFoundException {
    return requestRepository.get(requestId)
        .orElseThrow(() -> new EntityNotFoundException("Request not found: " + requestId));
  }

  private RequestExecution getExecutionFromRequest(ManagerRequest request, int sequenceId) throws EntityNotFoundException {
    RequestExecution execution = request.getRequestExecutionBySeqId(sequenceId);
    if (execution == null) {
      throw new EntityNotFoundException("Execution not found: " + sequenceId);
    }
    return execution;
  }

  // TODO add timeout / exception on connection failure
  private InputStream getResultStreamFromBroker(RequestExecution execution) throws IOException {
    int externalId = execution.getExternalId();
    ResponseWithMetadata response = brokerAdmin.getResult(externalId, 1); // TODO change this method to getRequestBundleExport(int requestid)
    return response.getInputStream();
  }

  private int getNextIterationOfFileDownload(RequestExecution execution) {
    List<DownloadEvent> downloadEvents = execution.getDownloadEvents();
    Set<String> uniqueFilenames = getUniqueFilenames(downloadEvents);
    return uniqueFilenames.size() + 1;
  }

  private Set<String> getUniqueFilenames(List<DownloadEvent> downloadEvents) {
    return downloadEvents.stream()
        .map(DownloadEvent::getFilename)
        .collect(Collectors.toSet());
  }

  private String generateResultFilename(int requestId, int executionId, int iteration) {
    return String.format("aktin-export_req%d-exec%d-iter%d.zip", requestId, executionId, iteration);
  }

  private DownloadEvent createNewDownloadEvent(String username, Set<String> userOrgs, InputStream resultStream, String filename)
      throws FileHashingException {
    DownloadEvent downloadEvent = downloadEventFactory.create();
    downloadEvent.setUsername(username);
    downloadEvent.setUserOrganizations(userOrgs);
    downloadEvent.setDownloadDate(Instant.now());
    String fileHash = fileHashGenerator.generateFileHash(resultStream);
    downloadEvent.setDownloadHash(fileHash);
    downloadEvent.setHashAlgorithm(fileHashGenerator.getHashAlgorithm());
    downloadEvent.setFilename(filename);
    return downloadEvent;
  }

  private void saveDownloadEventInRequest(ManagerRequest request, int sequenceId, DownloadEvent downloadEvent) {
    RequestExecution execution = getExecutionFromRequest(request, sequenceId);
    List<DownloadEvent> downloadEvents = execution.getDownloadEvents();
    downloadEvents.add(downloadEvent);
    requestRepository.save(request);
  }

  @Override
  public InputStream getStoredExecutionResults(int requestId, int sequenceId, int iteration, String username, Set<String> userOrgs)
      throws EntityNotFoundException {
    log.info("Downloading existing results for requestId={} sequenceId={} iteration={}", requestId, sequenceId, iteration);
    String filename = generateResultFilename(requestId, sequenceId, iteration);
    InputStream resultStream = getResultStreamFromRepository(filename);
    ManagerRequest request = getRequestFromRepository(requestId);
    // log result download
    DownloadEvent downloadEvent = createNewDownloadEvent(username, userOrgs, resultStream, filename);
    saveDownloadEventInRequest(request, sequenceId, downloadEvent);
    return resultStream;
  }

  private InputStream getResultStreamFromRepository(String filename) throws EntityNotFoundException {
    return resultRepository.get(filename)
        .orElseThrow(() -> new EntityNotFoundException("Result not found: " + filename));
  }

  @Override
  public void deleteExecutionResults(int requestId, int sequenceId) throws EntityNotFoundException {
    log.info("Deleting execution results for requestId={} sequenceId={}", requestId, sequenceId);
    ManagerRequest request = getRequestFromRepository(requestId);
    RequestExecution execution = getExecutionFromRequest(request, sequenceId);
    deleteDownloadedResults(execution);
  }

  private void deleteDownloadedResults(RequestExecution execution) {
    List<DownloadEvent> downloadEvents = execution.getDownloadEvents();
    if (!downloadEvents.isEmpty()) {
      Set<String> filenames = getUniqueFilenames(downloadEvents);
      filenames.forEach(resultRepository::delete);
    } else {
      log.info("Request has no downloaded results");
    }
  }
}
