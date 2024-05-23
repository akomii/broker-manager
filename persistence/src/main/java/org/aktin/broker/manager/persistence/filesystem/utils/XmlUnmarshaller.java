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

package org.aktin.broker.manager.persistence.filesystem.utils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataValidationException;
import org.xml.sax.SAXException;

public class XmlUnmarshaller<T> {

  private final JAXBContext jaxbContext;
  private final Validator schemaValidator;
  private final Class<T> type;

  public XmlUnmarshaller(JAXBContext jaxbContext, Validator schemaValidator, Class<T> type) {
    this.jaxbContext = jaxbContext;
    this.schemaValidator = schemaValidator;
    this.type = type;
  }

  public T unmarshal(File xmlFile) throws JAXBException, DataValidationException, IOException {
    try {
      schemaValidator.validate(new StreamSource(xmlFile));
    } catch (SAXException e) {
      throw new DataValidationException("Error during validation of " + xmlFile, e);
    }
    Unmarshaller unmarshaller = createUnmarshaller();
    return type.cast(unmarshaller.unmarshal(xmlFile));
  }

  private Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbContext.createUnmarshaller();
  }
}
