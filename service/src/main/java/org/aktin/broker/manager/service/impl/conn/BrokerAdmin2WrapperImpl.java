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

package org.aktin.broker.manager.service.impl.conn;

import java.io.IOException;
import java.net.URI;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client.ResponseWithMetadata;
import org.aktin.broker.client2.BrokerAdmin2;
import org.aktin.broker.client2.auth.ApiKeyAuthentication;
import org.aktin.broker.manager.service.api.conn.BrokerAdminWrapper;
import org.aktin.broker.manager.service.api.exceptions.BrokerException;
import org.aktin.broker.query.xml.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class responsible for initializing a {@link BrokerAdmin2} client, which provides access to administrative functions of a broker.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public class BrokerAdmin2WrapperImpl implements BrokerAdminWrapper {

  private static final Logger log = LoggerFactory.getLogger(BrokerAdmin2WrapperImpl.class);

  private final BrokerAdmin brokerAdmin;

  /**
   * Constructor for providing the broker URI and API key directly. Initializes the {@link BrokerAdmin2} client using the specified parameters.
   *
   * @param uriString The URI of the broker.
   * @param apiKey    The API key for authentication with the broker.
   */
  public BrokerAdmin2WrapperImpl(String uriString, String apiKey) {
    brokerAdmin = initBrokerAdmin(uriString, apiKey);
  }

  /**
   * Helper method to create and configure a {@link BrokerAdmin2} instance.
   *
   * @return The initialized and configured {@link BrokerAdmin2} instance (conforming to the {@link BrokerAdmin} interface).
   * @throws IllegalStateException If the required configuration variables (broker URI or API key) are invalid.
   */
  private BrokerAdmin initBrokerAdmin(String uriString, String apiKey) throws IllegalStateException {
    if (uriString == null || apiKey == null) {
      throw new IllegalStateException("Broker URI or API key is null. Initialization failed");
    }
    URI brokerUri = URI.create(uriString);
    BrokerAdmin2 admin = new BrokerAdmin2(brokerUri);
    admin.setAuthFilter(new ApiKeyAuthentication(apiKey));
    log.info("Initialized BrokerAdmin with URI: {}", brokerUri);
    return admin;
  }

  @Override
  public int allocateNewExecutionOnBroker() throws BrokerException {
    int externalId;
    try {
      externalId = brokerAdmin.createRequest();
    } catch (IOException e) {
      throw new BrokerException("Failed to create execution on broker", e);
    }
    log.info("Created new execution on broker");
    return externalId;
  }

  @Override
  public void publishExecutionOnBroker(QueryRequest request, int[] targetNodes) throws BrokerException {
    try {
      int externalId = request.getId();
      brokerAdmin.putRequestDefinition(externalId, "application/vnd.aktin.query.request+xml", request.toString());
      brokerAdmin.setRequestTargetNodes(externalId, targetNodes);
      brokerAdmin.publishRequest(externalId);
    } catch (IOException e) {
      throw new BrokerException("Failed to publish execution on broker", e);
    }
    log.info("Published execution on broker");
  }

  // TODO change getResult() to getRequestBundleExport(int requestid)
  // TODO add timeout / exception on connection failure??
  @Override
  public ResponseWithMetadata getExecutionResult(int externalId) throws BrokerException {
    ResponseWithMetadata result;
    try {
      result = brokerAdmin.getResult(externalId, 1);
    } catch (IOException e) {
      throw new BrokerException("Failed to retrieve execution result from broker", e);
    }
    log.info("Retrieved execution result from broker");
    return result;
  }

  @Override
  public void closeExecutionOnBroker(int externalId) throws BrokerException {
    try {
      brokerAdmin.closeRequest(externalId);
    } catch (IOException e) {
      throw new BrokerException("Failed to close execution on broker", e);
    }
    log.info("Closed execution on broker");
  }

  @Override
  public void deleteExecutionFromBroker(int externalId) throws BrokerException {
    try {
      brokerAdmin.deleteRequest(externalId);
    } catch (IOException e) {
      throw new BrokerException("Failed to delete execution from broker", e);
    }
    log.info("Deleted execution from broker");
  }
}
