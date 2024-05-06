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
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.persistence.filesystem.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.enums.RequestState;
import org.aktin.broker.manager.api.models.ManagerRequest;
import org.aktin.broker.manager.api.models.ModificationRecordEntry;
import org.aktin.broker.manager.api.models.RequestExecution;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SingleRequestImpl.class, name = "SingleRequest"),
    @JsonSubTypes.Type(value = SeriesRequestImpl.class, name = "SeriesRequest")
})
public abstract class AbstractManagerRequest<T extends QuerySchedule> implements ManagerRequest<T> {

  int id;

  Set<String> tags;

  Set<Integer> authorizedOrganizations;

  Set<Integer> targetNodes;

  RequestState state;

  List<ModificationRecordEntry> modificationRecord;

  List<RequestExecution> executions;

  Instant createdDate;

  String createdBy;

  Query query;

  public String getTitle() {
    return query.title;
  }

  public void setTitle(String title) {
    query.title = title;
  }

  public String getDescription() {
    return query.description;
  }

  public void setDescription(String description) {
    query.description = description;
  }

  public List<Element> getExtensions() {
    return query.extensions;
  }

  public void setExtensions(List<Element> extensions) {
    query.extensions = extensions;
  }

  public Principal getPrincipal() {
    return query.principal;
  }

  public void setPrincipal(Principal principal) {
    query.principal = principal;
  }
}
