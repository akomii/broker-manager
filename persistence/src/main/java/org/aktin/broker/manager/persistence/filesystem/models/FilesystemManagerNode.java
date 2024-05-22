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

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
import org.aktin.broker.xml.Node;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemManagerNode extends Node implements ManagerNode {

  @Min(value = 1, message = "Data version must be 1 or higher")
  int dataVersion;

  @NotNull(message = "API key cannot be null, but it can be an empty string")
  String apiKey;

  Set<@NotBlank String> tags;

  @Valid
  List<UserNote> userNotes;

  @Min(value = 1, message = "ID must be 1 or higher")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NotBlank(message = "Client DN is mandatory")
  @Pattern(regexp = "CN=[\\w ,'-]+,O=[\\w ,'-]+,L=[\\w ,'-]+", message = "Client DN must follow the pattern CN=XXX,O=XXX,L=XXX")
  public String getClientDN() {
    return clientDN;
  }

  public void setClientDN(String clientDN) {
    this.clientDN = clientDN;
  }

  @NotNull(message = "Last contact is mandatory")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  public Instant getLastContact() {
    return lastContact;
  }

  public void setLastContact(Instant lastContact) {
    this.lastContact = lastContact;
  }
}
