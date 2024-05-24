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

package org.aktin.broker.manager.persistence.filesystem.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.enums.RequestState;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.models.ModificationEntry;
import org.aktin.broker.manager.persistence.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.filesystem.adapters.InstantAdapter;
import org.aktin.broker.manager.persistence.filesystem.adapters.ModificationEntryAdapter;
import org.aktin.broker.manager.persistence.filesystem.adapters.RequestExecutionAdapter;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({FilesystemSingleRequest.class, FilesystemSeriesRequest.class})
@EqualsAndHashCode
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractManagerRequest<T extends QuerySchedule> implements ManagerRequest<T> {

  @XmlAttribute
  int dataVersion;

  int id;

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  Set<String> tags;

  @XmlElementWrapper(name = "authorizedOrganizations")
  @XmlElement(name = "authorizedOrganization")
  Set<Integer> authorizedOrganizations;

  @XmlElementWrapper(name = "targetNodes")
  @XmlElement(name = "targetNode")
  Set<Integer> targetNodes;

  RequestState requestState;

  @XmlElementWrapper(name = "modificationEntries")
  @XmlElement(name = "modificationEntry")
  @XmlJavaTypeAdapter(ModificationEntryAdapter.class)
  List<ModificationEntry> modificationEntries;

  @XmlElementWrapper(name = "requestExecutions")
  @XmlElement(name = "requestExecution")
  @XmlJavaTypeAdapter(RequestExecutionAdapter.class)
  List<RequestExecution> requestExecutions;

  @XmlJavaTypeAdapter(InstantAdapter.class)
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
