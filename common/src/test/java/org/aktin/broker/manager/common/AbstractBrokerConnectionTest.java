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

package org.aktin.broker.manager.common;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.manager.BrokerContainer;
import org.aktin.broker.manager.BrokerTestDataGenerator;
import org.aktin.broker.manager.api.common.BrokerAdminInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Container;

/**
 * Abstract base class for integration tests involving the AKTIN Broker and its administrative client. Sets up the following:
 * <ul>
 *   <li>A Testcontainers-managed Broker instance for testing.</li>
 *   <li>A {@link BrokerTestDataGenerator} for creating test data.</li>
 *   <li>A {@link BrokerAdmin} client, facilitating interaction with AKTIN Broker admin functions.</li>
 * </ul>
 * Note: Expects a properties file (`broker.properties`) to be present in your test resources' folder.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
abstract class AbstractBrokerConnectionTest {

  @Container
  private static final BrokerContainer CONTAINER = new BrokerContainer();

  private static final String PROPERTIES_PATH = "src/test/resources/broker.properties";

  public static BrokerTestDataGenerator generator;

  public static BrokerAdmin adminClient;

  @BeforeAll
  public static void setup() {
    CONTAINER.start();
    generator = new BrokerTestDataGenerator(buildBrokerUrl());
    writeBrokerConfigToProperties();
    System.setProperty("brokermanager.config.path", PROPERTIES_PATH);
    PropertiesFileResolverImpl resolver = new PropertiesFileResolverImpl();
    resolver.init();
    BrokerAdminInitializer initializer = new BrokerAdminInitializerImpl(resolver);
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

  private static void writeBrokerConfigToProperties() {
    try (OutputStream output = new FileOutputStream(PROPERTIES_PATH)) {
      Properties properties = new Properties();
      properties.setProperty("broker.url", buildBrokerUrl());
      properties.setProperty("broker.apikey", getDefaultAdminKey());
      properties.store(output, null);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write broker.properties", e);
    }
  }

  @AfterAll
  public static void teardown() {
    CONTAINER.stop();
    File propertiesFile = new File(PROPERTIES_PATH);
    if (propertiesFile.exists() && !propertiesFile.delete()) {
      System.err.println("Warning: Could not delete broker.properties");
    }
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
