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

package org.aktin.broker.manager.models;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.aktin.broker.manager.enums.RequestState;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

/**
 * Represents a data request within the AKTIN infrastructure. Corresponds to a {@link org.aktin.broker.query.xml.QueryRequest} with more
 * capabilities.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequest {

  int getId();

  void setId(int id);

  Set<String> getTags();

  void setTags(Set<String> tags);

  Set<Integer> getAuthorizedOrganizations();

  void setAuthorizedOrganizations(Set<Integer> authorizedOrganizations);

  Set<Integer> getTargetNodes();

  void setTargetNodes(Set<Integer> targetNodes);

  RequestState getRequestState();

  void setRequestState(RequestState requestState);

  List<TextEntry> getModificationEntries();

  void setModificationEntries(List<TextEntry> modificationEntries);

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

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getCreatedBy();

  void setCreatedBy(String creator);
}
