package org.aktin.broker.manager.persistence.daos;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.api.models.ManagerNode;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.xml.Node;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerNodeDao extends Node implements ManagerNode {

  Set<String> tags;

  List<NodeStatusInfo> nodeStatusInfos;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getClientDN() {
    return clientDN;
  }

  public void setClientDN(String clientDN) {
    this.clientDN = clientDN;
  }

  public Instant getLastContact() {
    return lastContact;
  }

  public void setLastContact(Instant lastContact) {
    this.lastContact = lastContact;
  }
}
