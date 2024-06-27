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

import java.io.IOException;
import org.aktin.broker.manager.model.api.models.ManagerNode;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestArchive;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.impl.filesystem.repositories.FsExecutionResultRepository;
import org.aktin.broker.manager.persistence.impl.filesystem.repositories.FsManagerNodeRepository;
import org.aktin.broker.manager.persistence.impl.filesystem.repositories.FsManagerRequestArchive;
import org.aktin.broker.manager.persistence.impl.filesystem.repositories.FsManagerRequestRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("filesystem")
public class PersistenceConfig {

  @Bean
  public ManagerNodeRepository fsManagerNodeRepository(
      @Qualifier("managerNodeXmlMarshaller") XmlMarshaller xmlMarshaller,
      @Qualifier("managerNodeXmlUnmarshaller") XmlUnmarshaller<ManagerNode> xmlUnmarshaller,
      @Value("${broker-manager.storage.directory.nodes}") String nodesDirectory) throws IOException {
    return new FsManagerNodeRepository(xmlMarshaller, xmlUnmarshaller, nodesDirectory);
  }

  @Bean
  public ManagerRequestRepository fsManagerRequestRepository(
      @Qualifier("managerRequestXmlMarshaller") XmlMarshaller xmlMarshaller,
      @Qualifier("managerRequestXmlUnmarshaller") XmlUnmarshaller<ManagerRequest> xmlUnmarshaller,
      @Value("${broker-manager.storage.directory.requests}") String requestsDirectory) throws IOException {
    return new FsManagerRequestRepository(xmlMarshaller, xmlUnmarshaller, requestsDirectory);
  }

  @Bean
  public ManagerRequestArchive fsManagerRequestArchive(
      @Qualifier("managerRequestXmlUnmarshaller") XmlUnmarshaller<ManagerRequest> xmlUnmarshaller,
      @Value("${broker-manager.storage.directory.requests}") String requestsDirectory,
      @Value("${broker-manager.storage.directory.archive}") String archiveDirectory) throws IOException {
    return new FsManagerRequestArchive(xmlUnmarshaller, requestsDirectory, archiveDirectory);
  }

  @Bean
  public ExecutionResultRepository fsExecutionResultsRepository(
      @Value("${broker-manager.storage.directory.results}") String resultsDirectory) throws IOException {
    return new FsExecutionResultRepository(resultsDirectory);
  }
}
