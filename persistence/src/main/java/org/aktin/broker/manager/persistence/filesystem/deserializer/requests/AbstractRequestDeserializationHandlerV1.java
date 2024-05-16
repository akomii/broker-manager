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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.aktin.broker.manager.persistence.api.enums.RequestExecutionState;
import org.aktin.broker.manager.persistence.api.enums.RequestState;
import org.aktin.broker.manager.persistence.api.models.ResultsDownloadEvent;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.models.ModificationRecordEntry;
import org.aktin.broker.manager.persistence.api.models.NodeStatusInfo;
import org.aktin.broker.manager.persistence.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.filesystem.deserializer.DeserializationHandler;
import org.aktin.broker.manager.persistence.filesystem.deserializer.MigrationHandler;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemResultsDownloadEvent;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemModificationRecordEntry;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemNodeStatusInfo;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemRequestExecution;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

abstract class AbstractRequestDeserializationHandlerV1<T extends ManagerRequest<? extends QuerySchedule>> extends DeserializationHandler<T> {

  private final DocumentBuilder builder;

  protected AbstractRequestDeserializationHandlerV1(int version, MigrationHandler<T> migrationHandlerChain) {
    super(version, migrationHandlerChain);
    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("Error initializing DocumentBuilder", e);
    }
  }

  protected T doSerialization(JsonNode node) {
    T request = createRequestInstance();
    request.setId(deserializeNumber(node, "id"));
    request.setTags(deserializeTextList(node, "tags"));
    request.setAuthorizedOrganizations(deserializeNumbersList(node, "authorizedOrganizations"));
    request.setTargetNodes(deserializeNumbersList(node, "targetNodes"));
    request.setState(RequestState.valueOf(node.get("state").asText()));
    request.setModificationRecord(deserializeModificationRecordEntries(node));
    request.setExecutions(deserializeRequestExecutions(node));
    request.setCreatedDate(deserializeDate(node, "createdDate"));
    request.setCreatedBy(deserializeText(node, "createdBy"));
    request.setQuery(deserializeQuery(node));
    return request;
  }

  protected abstract T createRequestInstance();

  private List<ModificationRecordEntry> deserializeModificationRecordEntries(JsonNode node) {
    JsonNode recordEntriesNode = node.get("modificationRecord");
    List<ModificationRecordEntry> recordEntries = new ArrayList<>();
    if (recordEntriesNode != null) {
      for (JsonNode recordEntryNode : recordEntriesNode) {
        FilesystemModificationRecordEntry recordEntry = new FilesystemModificationRecordEntry();
        recordEntry.setModificationDate(deserializeDate(recordEntryNode, "modificationDate"));
        recordEntry.setUsername(deserializeText(recordEntryNode, "username"));
        recordEntry.setClob(deserializeText(recordEntryNode, "clob"));
        recordEntries.add(recordEntry);
      }
    }
    return recordEntries;
  }

  private List<RequestExecution> deserializeRequestExecutions(JsonNode node) {
    JsonNode executionsNode = node.get("executions");
    List<RequestExecution> executions = new ArrayList<>();
    if (executionsNode != null) {
      for (JsonNode executionNode : executionsNode) {
        FilesystemRequestExecution execution = new FilesystemRequestExecution();
        execution.setSequenceId(deserializeNumber(executionNode, "sequenceId"));
        execution.setExternalId(deserializeNumber(executionNode, "externalId"));
        execution.setReferenceDate(deserializeDate(executionNode, "referenceDate"));
        execution.setExecutionDate(deserializeDate(executionNode, "executionDate"));
        execution.setScheduledPublishDate(deserializeDate(executionNode, "scheduledPublishDate"));
        execution.setPublishedDate(deserializeDate(executionNode, "publishedDate"));
        execution.setScheduledClosingDate(deserializeDate(executionNode, "scheduledClosingDate"));
        execution.setClosedDate(deserializeDate(executionNode, "closedDate"));
        execution.setScheduledArchiveDate(deserializeDate(executionNode, "scheduledArchiveDate"));
        execution.setArchivedDate(deserializeDate(executionNode, "archivedDate"));
        execution.setCreatedDate(deserializeDate(executionNode, "createdDate"));
        execution.setCreatedBy(deserializeText(executionNode, "createdBy"));
        execution.setState(RequestExecutionState.valueOf(node.get("state").asText()));
        execution.setNodeStatusInfos(deserializeNodeStatusInfos(node));
        execution.setResultsDownloadEvents(deserializeDownloadEvents(node));
      }
    }
    return executions;
  }

  private List<NodeStatusInfo> deserializeNodeStatusInfos(JsonNode node) {
    JsonNode statusInfosNode = node.get("nodeStatusInfos");
    List<NodeStatusInfo> statusInfos = new ArrayList<>();
    if (statusInfosNode != null) {
      for (JsonNode statusInfoNode : statusInfosNode) {
        FilesystemNodeStatusInfo statusInfo = new FilesystemNodeStatusInfo();
        statusInfo.setStatusMessage(deserializeText(statusInfoNode, "statusMessage"));
        statusInfo.setNode(deserializeNumber(statusInfoNode, "node"));
        statusInfo.setDeleted(deserializeDate(statusInfoNode, "deleted"));
        statusInfo.setRetrieved(deserializeDate(statusInfoNode, "retrieved"));
        statusInfo.setQueued(deserializeDate(statusInfoNode, "queued"));
        statusInfo.setProcessing(deserializeDate(statusInfoNode, "processing"));
        statusInfo.setCompleted(deserializeDate(statusInfoNode, "completed"));
        statusInfo.setRejected(deserializeDate(statusInfoNode, "rejected"));
        statusInfo.setFailed(deserializeDate(statusInfoNode, "failed"));
        statusInfo.setExpired(deserializeDate(statusInfoNode, "expired"));
        statusInfos.add(statusInfo);
      }
    }
    return statusInfos;
  }

  private List<ResultsDownloadEvent> deserializeDownloadEvents(JsonNode node) {
    JsonNode downloadEventsNode = node.get("downloadEvents");
    List<ResultsDownloadEvent> resultsDownloadEvents = new ArrayList<>();
    if (downloadEventsNode != null) {
      for (JsonNode downloadEventNode : downloadEventsNode) {
        FilesystemResultsDownloadEvent downloadEvent = new FilesystemResultsDownloadEvent();
        downloadEvent.setUsername(deserializeText(downloadEventNode, "username"));
        downloadEvent.setUserOrganizations(deserializeTextList(downloadEventNode, "userOrganizations"));
        downloadEvent.setDownloadDate(deserializeDate(downloadEventNode, "downloadDate"));
        downloadEvent.setDownloadHash(deserializeText(downloadEventNode, "downloadHash"));
        downloadEvent.setHashAlgorithm(deserializeText(downloadEventNode, "hashAlgorithm"));
        resultsDownloadEvents.add(downloadEvent);
      }
    }
    return resultsDownloadEvents;
  }

  private Query deserializeQuery(JsonNode node) {
    JsonNode queryNode = node.get("query");
    Query query = new Query();
    query.title = deserializeText(queryNode, "title");
    query.description = deserializeText(queryNode, "description");
    query.principal = deserializePrincipal(queryNode);
    query.schedule = deserializeQuerySchedule(queryNode);
    query.extensions = deserializeExtensions(queryNode);
    return query;
  }

  private Principal deserializePrincipal(JsonNode node) {
    JsonNode principalNode = node.get("principal");
    Principal principal = new Principal();
    principal.name = deserializeText(principalNode, "name");
    principal.organisation = deserializeText(principalNode, "organisation");
    principal.email = deserializeText(principalNode, "email");
    principal.phone = deserializeText(principalNode, "phone");
    return principal;
  }

  protected abstract QuerySchedule deserializeQuerySchedule(JsonNode node);

  private List<Element> deserializeExtensions(JsonNode node) {
    try {
      JsonNode extensionsNode = node.get("extensions");
      List<Element> extensions = new ArrayList<>();
      for (JsonNode extensionNode : extensionsNode) {
        String elementString = extensionNode.asText();
        Document document = builder.parse(new InputSource(new StringReader(elementString)));
        extensions.add(document.getDocumentElement());
      }
      return extensions;
    } catch (IOException | SAXException e) {
      throw new RuntimeException("Error deserializing extensions", e);
    }
  }
}
