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

package org.aktin.broker.manager.persistence.filesystem.validation;

import java.time.Instant;
import java.util.regex.Pattern;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.models.UserNote;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataValidationException;

public class ManagerNodeValidator {

  private static final Pattern CLIENT_DN_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,=+/\\\\.@;]+$");

  public void validate(ManagerNode node) throws DataValidationException {
    if (node.getId() <= 0) {
      throw new DataValidationException("Invalid ManagerNode ID");
    }
    if (node.getClientDN() == null || node.getClientDN().isEmpty() || !CLIENT_DN_PATTERN.matcher(node.getClientDN()).matches()) {
      throw new DataValidationException("Invalid ManagerNode clientDN");
    }
    if (node.getLastContact() == null || node.getLastContact().isAfter(Instant.now())) {
      throw new DataValidationException("Invalid ManagerNode lastContact");
    }
    if (node.getUserNotes() != null) {
      for (UserNote userNote : node.getUserNotes()) {
        validate(userNote);
      }
    }
  }

  private void validate(UserNote note) throws DataValidationException {
    if (note.getUsername() == null || note.getUsername().isEmpty()) {
      throw new DataValidationException("UserNote username cannot be empty");
    }
    if (note.getCreatedDate() == null || note.getCreatedDate().isAfter(Instant.now())) {
      throw new DataValidationException("UserNote createdDate is invalid");
    }
    if (note.getText() == null || note.getText().isEmpty()) {
      throw new DataValidationException("UserNote text cannot be empty");
    }
  }
}
