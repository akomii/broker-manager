package org.aktin.broker.manager.api.models;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface ManagerNode {

  Integer getId();

  void setId(Integer id);

  Set<String> getTags();

  void setTags(Set<String> tags);

  //TODO GETTER/SETTER notes

  String getClientDN();

  void setClientDN(String clientDN);

  Instant getLastContact();

  void setLastContact(Instant lastContact);

  List<NodeStatusInfo> getNodeStatusInfos();

  void setNodeStatusInfos(List<NodeStatusInfo> nodeStatusInfos);
}
