package org.aktin.broker.manager.persistence.daos;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.enums.RequestState;
import org.aktin.broker.manager.api.models.ManagerNode;
import org.aktin.broker.manager.api.models.ManagerRequest;
import org.aktin.broker.manager.api.models.RequestExecution;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractManagerRequest<T extends QuerySchedule> implements ManagerRequest<T> {

  Integer id;

  Set<String> tags;

  List<String> allowedOrgsToDownloadResults;

  Instant createdDate;

  String createdBy;

  RequestState state;

  List<ManagerNode> targetedManagerNodes;

  List<RequestExecution> requestExecutions;

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

  public String getPrincipalName() {
    return query.principal.name;
  }

  public void setPrincipalName(String name) {
    query.principal.name = name;
  }

  public String getPrincipalOrganisation() {
    return query.principal.organisation;
  }

  public void setPrincipalOrganisation(String organisation) {
    query.principal.organisation = organisation;
  }

  public String getPrincipalEmail() {
    return query.principal.email;
  }

  public void setPrincipalEmail(String email) {
    query.principal.email = email;
  }

  public Period getQueryDuration() {
    return query.schedule.duration;
  }

  public void setQueryDuration(Period duration) {
    query.schedule.duration = duration;
  }
}
