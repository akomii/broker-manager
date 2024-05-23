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

package org.aktin.broker.manager.persistence.filesystem.conf;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.aktin.broker.manager.persistence.filesystem.models.FilesystemManagerNode;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlMarshaller;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlUnmarshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("filesystem")
public class JaxbConfig {

  JAXBContext managerNodeJaxbContext() throws JAXBException {
    return JAXBContext.newInstance(FilesystemManagerNode.class);
  }

  Validator managerNodeSchemaValidator() throws JAXBException {
    return createSchemaValidator("manager_node.xsd");
  }

  @Bean
  public XmlMarshaller managerNodeXmlMarshaller() throws JAXBException {
    return new XmlMarshaller(managerNodeJaxbContext(), managerNodeSchemaValidator());
  }

  @Bean
  public XmlUnmarshaller<ManagerNode> managerNodeXmlUnmarshaller() throws JAXBException {
    return new XmlUnmarshaller<>(managerNodeJaxbContext(), managerNodeSchemaValidator(), ManagerNode.class);
  }

  JAXBContext managerRequestJaxbContext() throws JAXBException {
    return JAXBContext.newInstance(FilesystemSingleRequest.class, FilesystemSeriesRequest.class);
  }

  private Validator createSchemaValidator(String schemaFileName) throws JAXBException {
    String resourceName = "filesystem" + File.separator + schemaFileName;
    try {
      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = sf.newSchema(new StreamSource(getClass().getClassLoader().getResourceAsStream(resourceName)));
      return schema.newValidator();
    } catch (Exception e) {
      throw new JAXBException("Error loading XSD schema for validation: " + e.getMessage(), e);
    }
  }
}