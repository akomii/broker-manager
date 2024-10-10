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

package org.aktin.broker.manager.persistence.impl.filesystem.util;

import java.io.File;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlUnmarshaller<T> {

  private final JAXBContext jaxbContext;
  private final Class<T> type;

  public XmlUnmarshaller(JAXBContext jaxbContext, Class<T> type) {
    this.jaxbContext = jaxbContext;
    this.type = type;
  }

  public T unmarshal(File xmlFile) throws JAXBException {
    Unmarshaller unmarshaller = createUnmarshaller();
    return type.cast(unmarshaller.unmarshal(xmlFile));
  }

  public T unmarshal(InputStream inputStream) throws JAXBException {
    Unmarshaller unmarshaller = createUnmarshaller();
    return type.cast(unmarshaller.unmarshal(inputStream));
  }

  private Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbContext.createUnmarshaller();
  }
}
