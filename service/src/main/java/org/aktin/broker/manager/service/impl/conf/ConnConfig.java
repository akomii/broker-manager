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
import org.aktin.broker.manager.service.api.conn.BrokerAdminInitializer;
import org.aktin.broker.manager.service.impl.conn.BrokerAdmin2InitializerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnConfig {

  @Bean
  public BrokerAdmin brokerAdmin(
      @Value("${broker-manager.connection.broker.uri}") String uriString,
      @Value("${broker-manager.connection.broker.apiKey}") String apiKey) {
    BrokerAdminInitializer adminInitializer = new BrokerAdmin2InitializerImpl(uriString, apiKey);
    return adminInitializer.getAdminClient();
  }
}
