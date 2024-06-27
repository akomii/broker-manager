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
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.models.ManagerNode;
import org.aktin.broker.manager.model.models.TextEntry;
import org.aktin.broker.xml.Node;

@XmlRootElement(name = "managerNode")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsManagerNode implements ManagerNode {

  @XmlAttribute
  int dataVersion;

  String apiKey;

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  Set<String> tags;

  @XmlElementWrapper(name = "userNotes")
  @XmlElement(name = "userNote")
  List<TextEntry> userNotes;

  @XmlElement(namespace = org.aktin.broker.xml.XMLConstants.XML_NAMESPACE)
  Node node;

  public int getId() {
    return node.id;
  }

  public void setId(int id) {
    node.id = id;
  }

  public String getClientDN() {
    return node.clientDN;
  }

  public void setClientDN(String clientDN) {
    node.clientDN = clientDN;
  }

  @Override
  public Instant getLastContact() {
    return node.lastContact;
  }

  @Override
  public void setLastContact(Instant lastContact) {
    node.lastContact = lastContact;
  }
}
