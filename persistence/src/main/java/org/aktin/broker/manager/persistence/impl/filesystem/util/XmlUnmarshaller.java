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
import org.aktin.broker.manager.persistence.impl.filesystem.exceptions.DataMigrationException;
import org.aktin.broker.manager.persistence.impl.filesystem.migration.AbstractMigrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlUnmarshaller<T> {

  private static final Logger log = LoggerFactory.getLogger(XmlUnmarshaller.class);

  private final JAXBContext jaxbContext;
  private final Class<T> type;

  @Setter
  private AbstractMigrationHandler<T> migrationChain = null;

  @Setter
  private int latestVersion = 0;

  public XmlUnmarshaller(JAXBContext jaxbContext, Class<T> type) {
    this.jaxbContext = jaxbContext;
    this.type = type;
  }

  public T unmarshal(File xmlFile)
      throws JAXBException, DataMigrationException, IllegalArgumentException, IOException, SAXException, ParserConfigurationException {
    Unmarshaller unmarshaller = createUnmarshaller();
    if (migrationChain != null) {
      Document xmlDocument = readXmlFile(xmlFile);
      xmlDocument = migrateIfNeeded(xmlDocument, xmlFile.getAbsolutePath());
      return type.cast(unmarshaller.unmarshal(new DOMSource(xmlDocument)));
    }
    return type.cast(unmarshaller.unmarshal(xmlFile));
  }

  private Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbContext.createUnmarshaller();
  }

  private Document migrateIfNeeded(Document xmlDocument, String xmlPath) throws DataMigrationException, IllegalArgumentException {
    int dataVersion = getDataVersion(xmlDocument);
    if (dataVersion < latestVersion) {
      AbstractMigrationHandler<T> migrationHandler = findHandlerByFromVersion(dataVersion);
      if (migrationHandler != null) {
        log.info("Migrating XML content of {} from version {} to latest version {}", xmlPath, dataVersion, latestVersion);
        xmlDocument = migrationHandler.migrate(xmlDocument);
      } else {
        throw new IllegalArgumentException("No migration handler found for: " + xmlPath);
      }
    }
    return xmlDocument;
  }

  private Document readXmlFile(File xmlFile) throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    dbFactory.setNamespaceAware(true);
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    return dBuilder.parse(xmlFile);
  }

  private int getDataVersion(Document xmlDocument) {
    return Integer.parseInt(xmlDocument.getDocumentElement().getAttribute("dataVersion"));
  }

  private AbstractMigrationHandler<T> findHandlerByFromVersion(int dataVersion) {
    AbstractMigrationHandler<T> handler = migrationChain;
    while (handler != null) {
      if (handler.getFromVersion() == dataVersion) {
        return handler;
      }
      handler = handler.getSuccessor();
    }
    return null;
  }
}
