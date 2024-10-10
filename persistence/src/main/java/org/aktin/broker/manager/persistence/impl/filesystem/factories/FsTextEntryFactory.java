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

package org.aktin.broker.manager.persistence.impl.filesystem.factories;

import java.time.Instant;
import org.aktin.broker.manager.model.api.factories.TextEntryFactory;
import org.aktin.broker.manager.model.api.models.TextEntry;
import org.aktin.broker.manager.persistence.impl.filesystem.models.FsTextEntry;

public class FsTextEntryFactory implements TextEntryFactory {

  @Override
  public TextEntry create(String username, Instant createdDate, String content) {
    return new FsTextEntry(username, createdDate, content);
  }
}
