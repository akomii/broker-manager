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
 * MERCHANTABILITY or FITNESS FOR T PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.persistence.filesystem.deserializer;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class DeserializationHandler<T> {

  private MigrationHandler<T> migrationChainRoot;

  protected DeserializationHandler(MigrationHandler<T> migrationHandlerChain) {
    migrationChainRoot = findHandlerByFromVersion(migrationHandlerChain);
  }

  private MigrationHandler<T> findHandlerByFromVersion(MigrationHandler<T> handlerChain) {
    while (handlerChain != null) {
      if (handlerChain.getFromVersion() == getVersion()) {
        return handlerChain;
      }
      handlerChain = handlerChain.getSuccessor();
    }
    return null;
  }

  public T deserialize(JsonNode node) {
    T entity = doSerialization(node);
    if (migrationChainRoot != null) {
      entity = migrationChainRoot.migrate(entity);
    }
    return entity;
  }

  public abstract int getVersion();

  protected abstract T doSerialization(JsonNode node);
}
