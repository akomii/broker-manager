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

package org.aktin.broker.manager.persistence.filesystem.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

abstract class AbstractXmlAdapter<F extends D, D> extends XmlAdapter<F, D> {

  private final Class<F> filesystemClass;

  AbstractXmlAdapter(Class<F> filesystemClass) {
    this.filesystemClass = filesystemClass;
  }

  @Override
  public D unmarshal(F filesystemObject) {
    return filesystemObject;
  }

  @Override
  public F marshal(D domainObject) {
    if (filesystemClass.isInstance(domainObject)) {
      return filesystemClass.cast(domainObject);
    }
    throw new IllegalArgumentException("Invalid instance type of " + filesystemClass);
  }
}
