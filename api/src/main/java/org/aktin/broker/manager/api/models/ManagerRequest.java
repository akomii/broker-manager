package org.aktin.broker.manager.api.models;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aktin.broker.manager.api.enums.RequestState;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QuerySchedule;
import org.w3c.dom.Element;

public interface ManagerRequest<T extends QuerySchedule> {

  Integer getId();

  void setId(Integer id);

  Set<String> getTags();

  void setTags(Set<String> tags);

  List<String> getAllowedOrgsToDownloadResults();

  void setAllowedOrgsToDownloadResults(List<String> allowedOrgs);

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getCreatedBy();

  void setCreatedBy(String creator);

  //TODO modifierLog

  RequestState getState();

  void setState(RequestState state);

  List<ManagerNode> getTargetedManagerNodes();

  void setTargetedManagerNodes(List<ManagerNode> targetedManagerNodes);

  List<RequestExecution> getRequestExecutions();

  void setRequestExecutions(List<RequestExecution> requestExecutions);

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

  String getPrincipalName();

  void setPrincipalName(String name);

  String getPrincipalOrganisation();

  void setPrincipalOrganisation(String organisation);

  String getPrincipalEmail();

  void setPrincipalEmail(String email);

  T getQuerySchedule();

  void setQuerySchedule(T schedule);

  Period getQueryDuration();

  void setQueryDuration(Period duration);
}
