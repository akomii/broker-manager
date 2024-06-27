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

package org.aktin.broker.manager.persistence.impl.filesystem.conf;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.aktin.broker.manager.model.api.models.ManagerNode;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.impl.filesystem.models.FsManagerNode;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.models.FsSeriesRequest;
import org.aktin.broker.manager.persistence.impl.filesystem.models.FsSingleRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("filesystem")
public class JaxbConfig {

  private JAXBContext managerNodeJaxbContext() throws JAXBException {
    return JAXBContext.newInstance(FsManagerNode.class);
  }

  @Bean
  public XmlMarshaller managerNodeXmlMarshaller() throws JAXBException {
    return new XmlMarshaller(managerNodeJaxbContext());
  }

  @Bean
  public XmlUnmarshaller<ManagerNode> managerNodeXmlUnmarshaller() throws JAXBException {
    return new XmlUnmarshaller<>(managerNodeJaxbContext(), ManagerNode.class);
  }

  private JAXBContext managerRequestJaxbContext() throws JAXBException {
    return JAXBContext.newInstance(FsSingleRequest.class, FsSeriesRequest.class);
  }

  @Bean
  public XmlMarshaller managerRequestXmlMarshaller() throws JAXBException {
    return new XmlMarshaller(managerRequestJaxbContext());
  }

  @Bean
  public XmlUnmarshaller<ManagerRequest> managerRequestXmlUnmarshaller() throws JAXBException {
    return new XmlUnmarshaller<>(managerRequestJaxbContext(), ManagerRequest.class);
  }
}
