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

import lombok.Getter;

public abstract class MigrationHandler<T> {

  @Getter
  private MigrationHandler<T> successor;

  public abstract int getFromVersion();

  public abstract int getToVersion();

  public void setSuccessor(MigrationHandler<T> successor) {
    if (successor != null && successor.getFromVersion() != getToVersion()) {
      throw new IllegalArgumentException("Successor's fromVersion must align with this handler's toVersion");
    }
    this.successor = successor;
  }

  public T migrate(T entity) {
    T migratedEntity = doMigration(entity);
    if (successor != null) {
      migratedEntity = successor.migrate(migratedEntity);
    }
    return migratedEntity;
  }

  protected abstract T doMigration(T entity);
}
