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
import java.io.StringReader;
import java.nio.file.Files;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import lombok.Setter;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataValidationException;
import org.aktin.broker.manager.persistence.filesystem.migration.MigrationHandler;
import org.xml.sax.SAXException;

//TODO get latestSchemaVersion directly from Schema or Validator
public class XmlUnmarshaller<T> {

  private final JAXBContext jaxbContext;
  private final Validator schemaValidator;
  private final Class<T> type;

  @Setter
  private MigrationHandler<T> migrationChain = null;

  @Setter
  private int latestSchemaVersion = 0;

  public XmlUnmarshaller(JAXBContext jaxbContext, Validator schemaValidator, Class<T> type) {
    this.jaxbContext = jaxbContext;
    this.schemaValidator = schemaValidator;
    this.type = type;
  }

  public T unmarshal(File xmlFile) throws JAXBException, DataValidationException, DataMigrationException, IOException {
    String xmlContent = readXmlFile(xmlFile);
    if (migrationChain != null) {
      int dataVersion = getDataVersion(xmlContent);
      if (dataVersion < latestSchemaVersion && migrationChain != null) {
        MigrationHandler<T> migrationHandler = findHandlerByFromVersion(dataVersion);
        if (migrationHandler != null) {
          xmlContent = migrationHandler.migrate(xmlContent);
        } else {
          throw new IllegalArgumentException("No migration handler found for version: " + dataVersion);
        }
      }
    }
    validateAgainstSchema(xmlContent);
    Unmarshaller unmarshaller = createUnmarshaller();
    return type.cast(unmarshaller.unmarshal(new StringReader(xmlContent)));
  }

  private Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbContext.createUnmarshaller();
  }

  private String readXmlFile(File xmlFile) throws IOException {
    return new String(Files.readAllBytes(xmlFile.toPath()));
  }

  private int getDataVersion(String xmlContent) {
    String versionString = xmlContent.replaceAll(".*dataVersion=\"(\\d+)\".*", "$1");
    return Integer.parseInt(versionString);
  }

  private MigrationHandler<T> findHandlerByFromVersion(int dataVersion) {
    MigrationHandler<T> handler = migrationChain;
    while (handler != null) {
      if (handler.getFromVersion() == dataVersion) {
        return handler;
      }
      handler = handler.getSuccessor();
    }
    return null;
  }

  private void validateAgainstSchema(String xmlContent) throws DataValidationException {
    try {
      schemaValidator.validate(new StreamSource(new StringReader(xmlContent)));
    } catch (SAXException | IOException e) {
      throw new DataValidationException("Error during validation of migrated XML content", e);
    }
  }
}
