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

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.io.IOException;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestArchive;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.repositories.FilesystemManagerNodeRepository;
import org.aktin.broker.manager.persistence.filesystem.repositories.FilesystemManagerRequestArchive;
import org.aktin.broker.manager.persistence.filesystem.repositories.FilesystemManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.validation.ManagerNodeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("filesystem")
public class PersistenceConfig {

  @Bean
  public Validator validator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }

  /*
  @Bean
  public ManagerRequestRepository filesystemManagerRequestRepository(@Autowired ObjectMapper objectMapper,
      @Value("${broker-manager.storage.directory.requests}") String storageDirectory) throws IOException {
    return new FilesystemManagerRequestRepository(objectMapper, storageDirectory);
  }

  @Bean
  public ManagerNodeRepository filesystemManagerNodeRepository(
      @Qualifier("managerNodeXmlMarshaller") XmlMarshaller xmlMarshaller,
      @Qualifier("managerNodeXmlUnmarshaller") XmlUnmarshaller<FilesystemManagerNode> xmlUnmarshaller,
      @Value("${broker-manager.storage.directory.nodes}") String storageDirectory
  ) throws IOException {
    return new FilesystemManagerNodeRepository(xmlMarshaller, xmlUnmarshaller, storageDirectory);
  }

  @Bean
  public ManagerRequestArchive filesystemManagerRequestArchive(@Autowired ObjectMapper objectMapper,
      @Value("${broker-manager.storage.directory.requests}") String requestStorage,
      @Value("${broker-manager.storage.directory.archive}") String requestArchive) throws IOException {
    return new FilesystemManagerRequestArchive(objectMapper, requestStorage, requestArchive);
  }
}
