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

package org.aktin.broker.manager.persistence.impl.filesystem.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.enums.RequestState;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.model.api.models.TextEntry;
import org.aktin.broker.manager.persistence.impl.filesystem.models.listener.ManagerRequestChangeListener;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({FsSingleRequest.class, FsSeriesRequest.class})
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractManagerRequest implements ManagerRequest {

  @XmlAttribute
  int dataVersion;

  @Getter
  @XmlTransient
  boolean dirty = false;

  @Setter
  @XmlTransient
  ManagerRequestChangeListener changeListener;

  int id;

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  Set<String> tags;

  @XmlElementWrapper(name = "authorizedOrgs")
  @XmlElement(name = "authorizedOrg")
  Set<Integer> authorizedOrganizations;

  @XmlElementWrapper(name = "targetNodes")
  @XmlElement(name = "targetNode")
  Set<Integer> targetNodes;

  RequestState requestState;

  @XmlElementWrapper(name = "modificationEntries")
  @XmlElement(name = "modificationEntry")
  List<TextEntry> modificationEntries;

  @XmlElementWrapper(name = "requestExecutions")
  @XmlElement(name = "requestExecution")
  List<RequestExecution> requestExecutions;

  Instant createdDate;
  String createdBy;

  @XmlElement(namespace = org.aktin.broker.xml.XMLConstants.XML_NAMESPACE)
  Query query;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    if (this.id != id) {
      this.id = id;
      markDirty();
    }
  }

  @Override
  public Set<String> getTags() {
    return tags != null ? Collections.unmodifiableSet(tags) : Collections.emptySet();
  }

  @Override
  public void setTags(Set<String> tags) {
    if (this.tags == null || !this.tags.equals(tags)) {
      this.tags = tags;
      markDirty();
    }
  }

  @Override
  public void addTag(String tag) {
    if (tags == null) {
      tags = new HashSet<>();
    }
    if (tags.add(tag)) {
      markDirty();
    }
  }

  @Override
  public void removeTag(String tag) {
    if (tags != null && tags.remove(tag)) {
      markDirty();
    }
  }

  @Override
  public Set<Integer> getAuthorizedOrganizations() {
    return authorizedOrganizations != null ? Collections.unmodifiableSet(authorizedOrganizations) : Collections.emptySet();
  }

  @Override
  public void setAuthorizedOrganizations(Set<Integer> authorizedOrganizations) {
    if (this.authorizedOrganizations == null || !this.authorizedOrganizations.equals(authorizedOrganizations)) {
      this.authorizedOrganizations = authorizedOrganizations;
      markDirty();
    }
  }

  @Override
  public void addAuthorizedOrganization(Integer orgId) {
    if (authorizedOrganizations == null) {
      authorizedOrganizations = new HashSet<>();
    }
    if (authorizedOrganizations.add(orgId)) {
      markDirty();
    }
  }

  @Override
  public void removeAuthorizedOrganization(Integer orgId) {
    if (authorizedOrganizations != null && authorizedOrganizations.remove(orgId)) {
      markDirty();
    }
  }

  @Override
  public Set<Integer> getTargetNodes() {
    return targetNodes != null ? Collections.unmodifiableSet(targetNodes) : Collections.emptySet();
  }

  @Override
  public void setTargetNodes(Set<Integer> targetNodes) {
    if (this.targetNodes == null || !this.targetNodes.equals(targetNodes)) {
      this.targetNodes = targetNodes;
      markDirty();
    }
  }

  @Override
  public void addTargetNode(Integer nodeId) {
    if (targetNodes == null) {
      targetNodes = new HashSet<>();
    }
    if (targetNodes.add(nodeId)) {
      markDirty();
    }
  }

  @Override
  public void removeTargetNode(Integer nodeId) {
    if (targetNodes != null && targetNodes.remove(nodeId)) {
      markDirty();
    }
  }

  @Override
  public RequestState getRequestState() {
    return requestState;
  }

  @Override
  public void setRequestState(RequestState requestState) {
    if (this.requestState != requestState) {
      this.requestState = requestState;
      markDirty();
    }
  }

  @Override
  public List<TextEntry> getModificationEntries() {
    return modificationEntries != null ? Collections.unmodifiableList(modificationEntries) : Collections.emptyList();
  }

  @Override
  public void setModificationEntries(List<TextEntry> modificationEntries) {
    if (this.modificationEntries == null || !this.modificationEntries.equals(modificationEntries)) {
      this.modificationEntries = modificationEntries;
      markDirty();
    }
  }

  @Override
  public void addModificationEntry(TextEntry entry) {
    if (modificationEntries == null) {
      modificationEntries = new ArrayList<>();
    }
    if (entry != null) {
      modificationEntries.add(entry);
      markDirty();
    }
  }

  @Override
  public List<RequestExecution> getRequestExecutions() {
    return requestExecutions != null ? Collections.unmodifiableList(requestExecutions) : Collections.emptyList();
  }

  @Override
  public void setRequestExecutions(List<RequestExecution> requestExecutions) {
    if (this.requestExecutions == null || !this.requestExecutions.equals(requestExecutions)) {
      if (this.requestExecutions != null) {
        for (RequestExecution exec : this.requestExecutions) {
          removeParentRefFromExecution(exec);
        }
      }
      this.requestExecutions = requestExecutions;
      if (this.requestExecutions != null) {
        for (RequestExecution exec : this.requestExecutions) {
          setParentRefToExecution(exec);
        }
      }
      markDirty();
    }
  }

  @Override
  public void addRequestExecution(RequestExecution execution) {
    if (requestExecutions == null) {
      requestExecutions = new ArrayList<>();
    }
    if (execution != null) {
      setParentRefToExecution(execution);
      requestExecutions.add(execution);
      markDirty();
    }
  }

  @Override
  public void removeRequestExecution(RequestExecution execution) {
    if (requestExecutions != null && requestExecutions.remove(execution)) {
      removeParentRefFromExecution(execution);
      markDirty();
    }
  }

  private void setParentRefToExecution(RequestExecution execution) {
    if (execution instanceof FsRequestExecution) {
      ((FsRequestExecution) execution).setParentRequestRef(this);
    }
  }

  private void removeParentRefFromExecution(RequestExecution execution) {
    if (execution instanceof FsRequestExecution) {
      ((FsRequestExecution) execution).setParentRequestRef(null);
    }
  }

  @Override
  public Instant getCreatedDate() {
    return createdDate;
  }

  @Override
  public void setCreatedDate(Instant createdDate) {
    if (!Objects.equals(this.createdDate, createdDate)) {
      this.createdDate = createdDate;
      markDirty();
    }
  }

  @Override
  public String getCreatedBy() {
    return createdBy;
  }

  @Override
  public void setCreatedBy(String createdBy) {
    if (!Objects.equals(this.createdBy, createdBy)) {
      this.createdBy = createdBy;
      markDirty();
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }

  @Override
  public void setQuery(Query query) {
    if (!Objects.equals(this.query, query)) {
      this.query = query;
      markDirty();
    }
  }

  @Override
  public String getTitle() {
    return query != null ? query.title : null;
  }

  @Override
  public void setTitle(String title) {
    initQueryIfNull();
    if (!Objects.equals(query.title, title)) {
      query.title = title;
      markDirty();
    }
  }

  @Override
  public String getDescription() {
    return query != null ? query.description : null;
  }

  @Override
  public void setDescription(String description) {
    initQueryIfNull();
    if (!Objects.equals(query.description, description)) {
      query.description = description;
      markDirty();
    }
  }

  @Override
  public List<Element> getExtensions() {
    return query != null ? query.extensions : null;
  }

  @Override
  public void setExtensions(List<Element> extensions) {
    initQueryIfNull();
    if (!Objects.equals(query.extensions, extensions)) {
      query.extensions = extensions;
      markDirty();
    }
  }

  @Override
  public Principal getPrincipal() {
    return query != null ? query.principal : null;
  }

  @Override
  public void setPrincipal(Principal principal) {
    initQueryIfNull();
    if (!Objects.equals(query.principal, principal)) {
      query.principal = principal;
      markDirty();
    }
  }

  protected void initQueryIfNull() {
    if (query == null) {
      query = new Query();
    }
  }

  protected void markDirty() {
    this.dirty = true;
    notifyChangeListener();
  }

  public void clearDirty() {
    this.dirty = false;
  }

  protected void notifyChangeListener() {
    if (changeListener != null) {
      changeListener.onManagerRequestChange(this);
    }
  }
}
