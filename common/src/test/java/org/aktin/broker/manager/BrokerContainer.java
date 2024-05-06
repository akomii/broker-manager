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

package org.aktin.broker.manager;

import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

/**
 * Provides a Testcontainers wrapper for managing an AKTIN Broker instance. This class simplifies spinning up a Docker container, configuring it for
 * AKTIN Broker, and handling access to its port.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public class BrokerContainer extends GenericContainer<BrokerContainer> {

  private static final int BROKER_PORT = 8080;
  private static final String BROKER_IMAGE = "ghcr.io/aktin/aktin-broker:1.3.4";
  private static final WaitStrategy WAIT_STRATEGY = Wait.forHttp("/broker/status").forStatusCode(200);
  private static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(20);

  public BrokerContainer() {
    super(BROKER_IMAGE);
    withExposedPorts(BROKER_PORT);
    waitingFor(WAIT_STRATEGY);
    withStartupTimeout(STARTUP_TIMEOUT);
    withReuse(true);
  }

  public int getBrokerPort() {
    return getMappedPort(BROKER_PORT);
  }
}
