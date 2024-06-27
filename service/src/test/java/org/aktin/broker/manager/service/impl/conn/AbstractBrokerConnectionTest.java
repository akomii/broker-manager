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

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.manager.service.BrokerContainer;
import org.aktin.broker.manager.service.BrokerTestDataGenerator;
import org.aktin.broker.manager.service.api.conn.BrokerAdminInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Container;

/**
 * Abstract base class for integration tests involving the AKTIN Broker and its administrative {@link BrokerAdmin} client. Sets up the following:
 * <ul>
 *   <li>A Testcontainers-managed Broker instance for testing.</li>
 *   <li>A {@link BrokerTestDataGenerator} for creating test data.</li>
 *   <li>A {@link BrokerAdmin} client, facilitating interaction with AKTIN Broker admin functions.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
abstract class AbstractBrokerConnectionTest {

  @Container
  private static final BrokerContainer CONTAINER = new BrokerContainer();

  public static BrokerTestDataGenerator generator;

  public static BrokerAdmin adminClient;

  @BeforeAll
  public static void setup() {
    CONTAINER.start();
    generator = new BrokerTestDataGenerator(buildBrokerUrl());
    BrokerAdminInitializer initializer = new BrokerAdmin2InitializerImpl(buildBrokerUrl(), getDefaultAdminKey());
    adminClient = initializer.getAdminClient();
  }

  private static String buildBrokerUrl() {
    URIBuilder builder = new URIBuilder();
    builder
        .setScheme("http")
        .setHost(CONTAINER.getHost())
        .setPort(CONTAINER.getBrokerPort())
        .setPath("broker/");
    return builder.toString();
  }

  @AfterAll
  public static void teardown() {
    CONTAINER.stop();
  }

  public static String getDefaultAdminKey() {
    return "xxxAdmin1234";
  }

  public static String getDefaultKey1() {
    return "xxxApiKey123";
  }

  public static String getDefaultKey2() {
    return "xxxApiKey567";
  }

  public static String getDefaultKey3() {
    return "xxxApiKey890";
  }
}
