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

import java.io.IOException;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.repositories.FsManagerNodeRepository;
import org.aktin.broker.manager.persistence.filesystem.repositories.FsManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlMarshaller;
import org.aktin.broker.manager.persistence.filesystem.utils.XmlUnmarshaller;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("filesystem")
public class PersistenceConfig {

  @Bean
  public ManagerNodeRepository filesystemManagerNodeRepository(
      @Qualifier("managerNodeXmlMarshaller") XmlMarshaller xmlMarshaller,
      @Qualifier("managerNodeXmlUnmarshaller") XmlUnmarshaller<ManagerNode> xmlUnmarshaller,
      @Value("${broker-manager.storage.directory.nodes}") String storageDirectory
  ) throws IOException {
    return new FsManagerNodeRepository(xmlMarshaller, xmlUnmarshaller, storageDirectory);
  }

  @Bean
  public ManagerRequestRepository filesystemManagerRequestRepository(
      @Qualifier("managerRequestXmlMarshaller") XmlMarshaller xmlMarshaller,
      @Qualifier("managerRequestXmlUnmarshaller") XmlUnmarshaller<ManagerRequest> xmlUnmarshaller,
      @Value("${broker-manager.storage.directory.requests}") String storageDirectory
  ) throws IOException {
    return new FsManagerRequestRepository(xmlMarshaller, xmlUnmarshaller, storageDirectory);
  }
}
