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

package org.aktin.broker.manager.service.util;

import java.net.URI;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client2.BrokerAdmin2;
import org.aktin.broker.client2.auth.ApiKeyAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for initializing a {@link BrokerAdmin2} client, which provides access to administrative functions of a broker.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
@Service
class BrokerAdmin2InitializerImpl implements BrokerAdminInitializer {

  private static final Logger log = LoggerFactory.getLogger(BrokerAdmin2InitializerImpl.class);

  private final String uriString;
  private final String apiKey;
  private final BrokerAdmin brokerAdmin;

  /**
   * Constructor for providing the broker URI and API key directly. Initializes the {@link BrokerAdmin2} client using the specified parameters.
   *
   * @param uriString The URI of the broker.
   * @param apiKey    The API key for authentication with the broker.
   */
  public BrokerAdmin2InitializerImpl(
      @Value("${broker-manager.connection.broker.uri}") String uriString,
      @Value("${broker-manager.connection.broker.apiKey}") String apiKey) {
    this.uriString = uriString;
    this.apiKey = apiKey;
    brokerAdmin = initBrokerAdmin();
  }

  /**
   * Helper method to create and configure a {@link BrokerAdmin2} instance.
   *
   * @return The initialized and configured {@link BrokerAdmin2} instance (conforming to the {@link BrokerAdmin} interface).
   * @throws IllegalStateException If the required configuration variables (broker URI or API key) are invalid.
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
