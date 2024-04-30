/*
 *    Copyright (c) 2024  AKTIN
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.api.enums;

/**
 * Possible configuration keys used within the broker-manager properties file.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public enum PropertiesKey {

  /**
   * The URL of the AKTIN Broker.
   */
  URL("broker.url"),

  /**
   * The ADMIN API key used to authenticate with the broker.
   */
  APIKEY("broker.apikey");

  private final String value;

  /**
   * Constructor for the PropertiesKey enum. Assigns the provided String value to the 'value' field.
   *
   * @param value The String value representing the properties file key.
   */
  PropertiesKey(String value) {
    this.value = value;
  }

  /**
   * Overrides the default toString() method to return the String value associated with the PropertiesKey instance.
   *
   * @return The String representation of the properties file key.
   */
  @Override
  public String toString() {
    return this.value;
  }
}
