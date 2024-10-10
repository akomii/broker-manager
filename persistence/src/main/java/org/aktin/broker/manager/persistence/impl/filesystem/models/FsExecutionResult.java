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

import java.io.InputStream;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.aktin.broker.manager.model.api.models.ExecutionResult;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FsExecutionResult extends FsExecutionResultMetadata implements ExecutionResult {

  InputStream content;

  public FsExecutionResult(String id, String contentType, InputStream content, String contentHash, String hashAlgorithm) {
    this.id = id;
    this.contentType = contentType;
    this.content = content;
    this.contentHash = contentHash;
    this.hashAlgorithm = hashAlgorithm;
  }
}
