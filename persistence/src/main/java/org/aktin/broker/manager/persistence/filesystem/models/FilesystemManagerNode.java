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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.models.UserNote;
import org.aktin.broker.manager.persistence.filesystem.adapters.InstantAdapter;
import org.aktin.broker.manager.persistence.filesystem.adapters.UserNoteAdapter;

@XmlRootElement(name = "managerNode")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemManagerNode implements ManagerNode {

  @XmlAttribute
  int dataVersion;

  int id;

  String apiKey;

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  Set<String> tags;

  @XmlElementWrapper(name = "userNotes")
  @XmlElement(name = "userNote")
  @XmlJavaTypeAdapter(UserNoteAdapter.class)
  List<UserNote> userNotes;

  String clientDN;

  @XmlJavaTypeAdapter(InstantAdapter.class)
  Instant lastContact;
}
