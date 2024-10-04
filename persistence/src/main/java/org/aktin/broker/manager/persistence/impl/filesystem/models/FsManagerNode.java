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
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.models.ManagerNode;
import org.aktin.broker.manager.model.api.models.TextEntry;
import org.aktin.broker.manager.persistence.impl.filesystem.models.listener.ManagerNodeChangeListener;
import org.aktin.broker.xml.Node;

@XmlRootElement(name = "managerNode")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsManagerNode implements ManagerNode {

  @XmlAttribute
  int dataVersion;

  @XmlTransient
  boolean dirty = false;

  @Setter
  @XmlTransient
  ManagerNodeChangeListener changeListener;

  @Getter
  boolean active;

  @Getter
  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  Set<String> tags;

  // UserNote entries are not modifiable
  @XmlElementWrapper(name = "userNotes")
  @XmlElement(name = "userNote")
  List<TextEntry> userNotes;

  @XmlElement(namespace = org.aktin.broker.xml.XMLConstants.XML_NAMESPACE)
  Node node;


  @Override
  public void setActive(boolean active) {
    if (this.active != active) {
      this.active = active;
      markDirty();
    }
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
  public List<TextEntry> getUserNotes() {
    return userNotes != null ? Collections.unmodifiableList(userNotes) : Collections.emptyList();
  }

  @Override
  public void setUserNotes(List<TextEntry> userNotes) {
    if (this.userNotes == null || !this.userNotes.equals(userNotes)) {
      this.userNotes = userNotes;
      markDirty();
    }
  }

  @Override
  public void addUserNote(TextEntry note) {
    if (userNotes == null) {
      userNotes = new ArrayList<>();
    }
    if (note != null) {
      userNotes.add(note);
      markDirty();
    }
  }

  @Override
  public void removeUserNote(TextEntry note) {
    if (userNotes != null && userNotes.remove(note)) {
      markDirty();
    }
  }

  @Override
  public int getId() {
    return node != null ? node.id : 0;
  }

  @Override
  public void setId(int id) {
    if (node == null) {
      initDefaultNode();
    }
    if (node.id != id) {
      node.id = id;
      markDirty();
    }
  }

  @Override
  public String getClientDN() {
    return node != null ? node.clientDN : null;
  }

  @Override
  public void setClientDN(String clientDN) {
    if (node == null) {
      initDefaultNode();
    }
    if (node.clientDN == null || !node.clientDN.equals(clientDN)) {
      node.clientDN = clientDN;
      markDirty();
    }
  }

  @Override
  public Instant getLastContact() {
    return node != null ? node.lastContact : null;
  }

  @Override
  public void setLastContact(Instant lastContact) {
    if (node == null) {
      initDefaultNode();
    }
    if (node.lastContact == null || !node.lastContact.equals(lastContact)) {
      node.lastContact = lastContact;
      markDirty();
    }
  }

  private void initDefaultNode() {
    this.node = new Node(0, "", Instant.MIN);
  }

  private void markDirty() {
    this.dirty = true;
    notifyChangeListener();
  }

  public void clearDirty() {
    this.dirty = false;
  }

  private void notifyChangeListener() {
    if (changeListener != null) {
      changeListener.onManagerNodeChanged(this);
    }
  }
}
