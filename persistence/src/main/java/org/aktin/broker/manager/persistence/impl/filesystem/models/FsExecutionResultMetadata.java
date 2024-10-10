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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.models.ExecutionResult;

@XmlRootElement(name = "executionResultMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class FsExecutionResultMetadata {

  String name;
  String contentType;
  String contentHash;
  String hashAlgorithm;

  public FsExecutionResultMetadata(ExecutionResult result) {
    this.name = result.getName();
    this.contentType = result.getContentType();
    this.contentHash = result.getContentHash();
    this.hashAlgorithm = result.getHashAlgorithm();
  }
}
