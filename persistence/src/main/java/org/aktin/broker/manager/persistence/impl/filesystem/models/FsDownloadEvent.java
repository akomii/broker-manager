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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.models.DownloadEvent;
import org.aktin.broker.manager.model.api.models.ExecutionResult;

@XmlRootElement(name = "downloadEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsDownloadEvent implements DownloadEvent {

  String username;

  @XmlElementWrapper(name = "userOrgs")
  @XmlElement(name = "userOrg")
  Set<String> userOrganizations;

  Instant downloadDate;
  ExecutionResult executionResult;

  public Set<String> getUserOrganizations() {
    return userOrganizations != null ? Collections.unmodifiableSet(userOrganizations) : Collections.emptySet();
  }

  public void setUserOrganizations(Set<String> userOrganizations) {
    if (userOrganizations != null) {
      this.userOrganizations = new HashSet<>(userOrganizations);
    } else {
      this.userOrganizations = null;
    }
  }
}
