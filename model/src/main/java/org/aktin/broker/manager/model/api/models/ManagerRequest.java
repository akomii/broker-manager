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

package org.aktin.broker.manager.model.api.models;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.aktin.broker.manager.model.api.enums.RequestState;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

/**
 * Represents a data request within the AKTIN infrastructure. A {@code ManagerRequest} is used to define queries that are executed by participating
 * nodes to collect research data.
 *
 * <p>The request includes:</p>
 * <ul>
 *   <li><strong>Tags</strong>: For categorization and filtering.</li>
 *   <li><strong>Authorized Organizations</strong>: Organizations permitted to access the request.</li>
 *   <li><strong>Target Nodes</strong>: Specific {@link ManagerNode}s where the request should be executed.</li>
 *   <li><strong>Request state</strong>: The current state of the request (see {@link RequestState}).</li>
 *   <li><strong>Modification Entries</strong>: Logs of changes made to the request.</li>
 *   <li><strong>Query Definition</strong>: The actual query to be executed.</li>
 *   <li><strong>Title</strong>: The human-readable title of the query.</li>
 *   <li><strong>Description</strong>: The human-readable description of the query.</li>
 *   <li><strong>Extensions</strong>: The technical syntax to be executed.</li>
 *   <li><strong>Principal</strong>: The author or owner of the request.</li>
 *   <li><strong>Schedule</strong>: Execution interval of the query.</li>
 *   <li><strong>Executions</strong>: Associated {@link RequestExecution}s of this request.</li>
 *   <li><strong>Creation information</strong>: Creation date and creator (user or system) of this request.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequest extends PersistentObject {

  Set<String> getTags();

  void setTags(Set<String> tags);

  void addTag(String tag);

  void removeTag(String tag);

  Set<Integer> getAuthorizedOrganizations();

  void setAuthorizedOrganizations(Set<Integer> authorizedOrganizations);

  void addAuthorizedOrganization(Integer organization);

  void removeAuthorizedOrganization(Integer organization);

  Set<Integer> getTargetNodes();

  void setTargetNodes(Set<Integer> targetNodes);

  void addTargetNode(Integer node);

  void removeTargetNode(Integer node);

  RequestState getRequestState();

  void setRequestState(RequestState requestState);

  List<TextEntry> getModificationEntries();

  void setModificationEntries(List<TextEntry> modificationEntries);

  void addModificationEntry(TextEntry entry);

  Query getQuery();

  void setQuery(Query query);

  String getTitle();

  void setTitle(String title);

  String getDescription();

  void setDescription(String description);

  List<Element> getExtensions();

  void setExtensions(List<Element> extensions);

  Principal getPrincipal();

  void setPrincipal(Principal principal);

  QuerySchedule getQuerySchedule();

  void setQuerySchedule(QuerySchedule schedule);

  List<RequestExecution> getRequestExecutions();

  void setRequestExecutions(List<RequestExecution> requestExecutions);

  void addRequestExecution(RequestExecution requestExecution);

  void removeRequestExecution(RequestExecution requestExecution);

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getCreatedBy();

  void setCreatedBy(String creator);
}
