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

package org.aktin.broker.manager.service.impl.conf;

import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.manager.model.api.factories.DownloadEventFactory;
import org.aktin.broker.manager.persistence.api.repositories.ExecutionResultRepository;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.service.api.conn.BrokerAdminWrapper;
import org.aktin.broker.manager.service.api.handlers.ExecutionResultHandler;
import org.aktin.broker.manager.service.impl.handler.ExecutionResultHandlerImpl;
import org.aktin.broker.manager.service.impl.util.SHA256Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

  @Bean
  public ExecutionResultHandler executionResultHandlerImpl(
      @Autowired BrokerAdminWrapper brokerAdminWrapper,
      @Autowired ExecutionResultRepository executionResultRepository,
      @Autowired ManagerRequestRepository managerRequestRepository,
      @Autowired DownloadEventFactory downloadEventFactory) {
    return new ExecutionResultHandlerImpl(
        brokerAdminWrapper,
        executionResultRepository,
        managerRequestRepository,
        downloadEventFactory,
        new SHA256Generator()
    );
  }
}
