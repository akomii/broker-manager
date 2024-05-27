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


import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class XmlMarshaller {

  private static final Logger log = LoggerFactory.getLogger(XmlMarshaller.class);
  private final JAXBContext jaxbContext;
  private final Validator schemaValidator;

  public XmlMarshaller(JAXBContext jaxbContext, Validator schemaValidator) {
    this.jaxbContext = jaxbContext;
    this.schemaValidator = schemaValidator;
  }

  public void marshal(Object objectToMarshal, File xmlFile) throws JAXBException, DataValidationException, IOException {
    File tempFile = createTempFile(xmlFile);
    try {
      Marshaller marshaller = createMarshaller();
      marshaller.marshal(objectToMarshal, tempFile);
      schemaValidator.validate(new StreamSource(tempFile));
      if (!tempFile.renameTo(xmlFile)) {
        throw new IOException("Failed to rename temporary file to " + xmlFile);
      }
    } catch (SAXException e) {
      throw new DataValidationException("Error during validation of " + tempFile, e);
    } finally {
      cleanUpTempFile(tempFile, xmlFile);
    }
  }

  private File createTempFile(File xmlFile) throws IOException {
    File parentDir = xmlFile.getParentFile();
    return File.createTempFile(xmlFile.getName(), ".tmp", parentDir);
  }

  private Marshaller createMarshaller() throws JAXBException {
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    return marshaller;
  }

  private void cleanUpTempFile(File tempFile, File xmlFile) {
    if (tempFile.exists() && !tempFile.equals(xmlFile)) {
      boolean deleted = tempFile.delete();
      if (!deleted) {
        log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
      }
    }
  }
}
