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
import java.util.List;
import java.util.Set;
import org.aktin.broker.manager.api.enums.RequestState;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

/**
 * Represents a data request within the AKTIN infrastructure. Consists of at least one {@link RequestExecution}. Corresponds to a
 * {@link org.aktin.broker.query.xml.QueryRequest} with more capabilities.
 *
 * @param <T> The specific type of {@link QuerySchedule} associated with the request
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequest<T extends QuerySchedule> {

  int getId();

  void setId(int id);

  Set<String> getTags();

  void setTags(Set<String> tags);

  Set<Integer> getAuthorizedOrganizations();

  void setAuthorizedOrganizations(Set<Integer> authorizedOrganizations);

  Set<Integer> getTargetNodes();

  void setTargetNodes(Set<Integer> targetNodes);

  RequestState getState();

  void setState(RequestState requestState);

  List<ModificationRecordEntry> getModificationRecord();

  void setModificationRecord(List<ModificationRecordEntry> modificationRecord);

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

  T getQuerySchedule();

  void setQuerySchedule(T schedule);

  List<RequestExecution> getExecutions();

  void setExecutions(List<RequestExecution> requestExecutions);

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getCreatedBy();

  void setCreatedBy(String creator);
}
