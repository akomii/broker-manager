/*
 *       Copyright (c) 2024  AKTIN
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Affero General Public License as
 *       published by the Free Software Foundation, either version 3 of the
 *       License, or (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Affero General Public License for more details.
 *
 *       You should have received a copy of the GNU Affero General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.api.common;

import org.aktin.broker.manager.api.enums.PropertiesKey;

/**
 * Interface to provide access to configuration values stored in a properties file.
 */
public interface PropertiesFileResolver {

  /**
   * Retrieves the value associated with the specified {@link PropertiesKey}.
   *
   * @param key The {@link PropertiesKey}} enum value representing the desired property.
   * @return The value of the property, or null if the key is not found.
   */
  String getKeyValue(PropertiesKey key);
}
