/*
 *    Copyright (c) 2024  AKTIN
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.common;

import java.net.URI;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client2.BrokerAdmin2;
import org.aktin.broker.client2.auth.ApiKeyAuthentication;
import org.aktin.broker.manager.api.common.BrokerAdminInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for initializing a {@link BrokerAdmin} client, which provides access to administrative functions of a broker.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
@Service
public class BrokerAdminInitializerImpl implements BrokerAdminInitializer {

  private static final Logger log = LoggerFactory.getLogger(BrokerAdminInitializerImpl.class);

  @Value("${broker-manager.connection.broker.uri}")
  private String uriString;

  @Value("${broker-manager.connection.broker.apiKey}")
  private String apiKey;

  private final BrokerAdmin brokerAdmin;

  public BrokerAdminInitializerImpl() {
    brokerAdmin = initBrokerAdmin();
  }

  public BrokerAdminInitializerImpl(String uriString, String apiKey) {
    this.uriString = uriString;
    this.apiKey = apiKey;
    brokerAdmin = initBrokerAdmin();
  }

  /**
   * Helper method to create and configure a {@link BrokerAdmin} instance.
   *
   * @return The initialized and configured {@link BrokerAdmin2} instance (conforming to the {@link BrokerAdmin} interface)
   * @throws IllegalStateException If the required configuration variables are invalid.
   */
  private BrokerAdmin initBrokerAdmin() throws IllegalStateException {
    if (uriString == null || apiKey == null) {
      throw new IllegalStateException("Broker URI or API key is null. Initialization failed");
    }
    URI brokerUri = URI.create(uriString);
    log.info("Initializing BrokerAdmin with URI: {}", brokerUri);
    BrokerAdmin2 admin = new BrokerAdmin2(brokerUri);
    admin.setAuthFilter(new ApiKeyAuthentication(apiKey));
    return admin;
  }

  /**
   * {@inheritDoc}
   */
  public BrokerAdmin getAdminClient() {
    return brokerAdmin;
  }
}
