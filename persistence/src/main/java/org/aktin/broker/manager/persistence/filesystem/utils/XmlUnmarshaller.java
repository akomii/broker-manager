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
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import lombok.Setter;
import org.aktin.broker.manager.persistence.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.filesystem.migration.MigrationHandler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlUnmarshaller<T> {

  private final JAXBContext jaxbContext;
  private final Class<T> type;

  @Setter
  private MigrationHandler<T> migrationChain = null;

  @Setter
  private int latestVersion = 0;

  public XmlUnmarshaller(JAXBContext jaxbContext, Class<T> type) {
    this.jaxbContext = jaxbContext;
    this.type = type;
  }

  public T unmarshal(File xmlFile) throws JAXBException, DataMigrationException, IOException, SAXException, ParserConfigurationException {
    Document xmlDocument = readXmlFile(xmlFile);
    if (migrationChain != null) {
      xmlDocument = migrateIfNeeded(xmlDocument);
    }
    Unmarshaller unmarshaller = createUnmarshaller();
    return type.cast(unmarshaller.unmarshal(new DOMSource(xmlDocument)));
  }

  private Document readXmlFile(File xmlFile) throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    return dBuilder.parse(xmlFile);
  }

  private Document migrateIfNeeded(Document xmlDocument) throws DataMigrationException, IllegalArgumentException {
    int dataVersion = getDataVersion(xmlDocument);
    if (dataVersion < latestVersion) {
      MigrationHandler<T> migrationHandler = findHandlerByFromVersion(dataVersion);
      if (migrationHandler != null) {
        xmlDocument = migrationHandler.migrate(xmlDocument);
      } else {
        throw new IllegalArgumentException("No migration handler found for version: " + dataVersion);
      }
    }
    return xmlDocument;
  }

  private int getDataVersion(Document xmlDocument) {
    return Integer.parseInt(xmlDocument.getDocumentElement().getAttribute("dataVersion"));
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

  private Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbContext.createUnmarshaller();
  }
}
