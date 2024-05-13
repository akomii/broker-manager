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
import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public abstract class DeserializationHandler<T> {

  private final MigrationHandler<T> migrationChainRoot;

  @Getter
  private final int version;

  protected DeserializationHandler(int version, MigrationHandler<T> migrationHandlerChain) {
    this.version = version;
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

  protected abstract T doSerialization(JsonNode node);

  protected String deserializeText(JsonNode node, String key) {
    return node.get(key).asText();
  }

  protected Set<String> deserializeTextList(JsonNode node, String key) {
    JsonNode textList = node.get(key);
    Set<String> texts = new HashSet<>();
    for (JsonNode text : textList) {
      texts.add(text.asText());
    }
    return texts;
  }

  protected int deserializeNumber(JsonNode node, String key) {
    return node.get(key).asInt();
  }

  protected Set<Integer> deserializeNumbersList(JsonNode node, String key) {
    JsonNode numbersList = node.get(key);
    Set<Integer> numbers = new HashSet<>();
    for (JsonNode number : numbersList) {
      numbers.add(number.asInt());
    }
    return numbers;
  }

  protected Instant deserializeDate(JsonNode node, String key) {
    return Instant.parse(node.get(key).asText());
  }

  protected boolean deserializeBool(JsonNode node, String key) {
    return node.get(key).asBoolean();
  }

  protected Period deserializePeriod(JsonNode node, String key) {
    return Period.parse(node.get(key).asText());
  }
}
