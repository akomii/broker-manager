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
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

/**
 * Testcontainers wrapper for managing an AKTIN Broker instance within tests.  It encapsulates the setup, configuration, and retrieval of essential
 * details for the broker.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public class BrokerContainer extends GenericContainer<BrokerContainer> {

  private static final int BROKER_PORT = 8080;
  private static final String BROKER_IMAGE = "ghcr.io/aktin/aktin-broker:latest";
  private static final WaitStrategy WAIT_STRATEGY = Wait.forHttp("/broker/status").forStatusCode(200);
  private static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(20);

  private final String brokerUrl;

  public BrokerContainer() {
    super(BROKER_IMAGE);
    withExposedPorts(BROKER_PORT);
    waitingFor(WAIT_STRATEGY);
    withStartupTimeout(STARTUP_TIMEOUT);
    withReuse(true);
    this.brokerUrl = initBrokerUrl();
  }

  private String initBrokerUrl() {
    URIBuilder builder = new URIBuilder();
    builder
        .setScheme("http")
        .setHost(getHost())
        .setPort(getMappedPort(8080))
        .setPath("broker/");
    return builder.toString();
  }

  public String getBrokerUrl() {
    return brokerUrl;
  }

  public String getDefaultAdminKey() {
    return "xxxAdmin1234";
  }

  public String getDefaultKey1() {
    return "xxxApiKey123";
  }

  public String getDefaultKey2() {
    return "xxxApiKey567";
  }

  public String getDefaultKey3() {
    return "xxxApiKey890";
  }
}
