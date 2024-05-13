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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aktin.broker.manager.persistence.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.models.ManagerRequest;
import org.aktin.broker.manager.persistence.filesystem.deserializer.nodes.ManagerNodeDeserializer;
import org.aktin.broker.manager.persistence.filesystem.deserializer.requests.ManagerRequestDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  private final ManagerRequestDeserializer managerRequestDeserializer;

  private final ManagerNodeDeserializer managerNodeDeserializer;

  public JacksonConfig(@Autowired ManagerRequestDeserializer managerRequestDeserializer, @Autowired ManagerNodeDeserializer managerNodeDeserializer) {
    this.managerRequestDeserializer = managerRequestDeserializer;
    this.managerNodeDeserializer = managerNodeDeserializer;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(ManagerRequest.class, managerRequestDeserializer);
    module.addDeserializer(ManagerNode.class, managerNodeDeserializer);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(module);
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }
}
