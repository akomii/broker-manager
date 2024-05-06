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

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.aktin.broker.client.BrokerClientImpl;
import org.aktin.broker.xml.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Verifies the functionalities of fetching broker node information and their associated properties, along with handling potential error scenarios
 * such as accessing non-existent resources.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrokerNodeTest extends AbstractBrokerConnectionTest {

  @BeforeAll
  public static void init() throws IOException {
    initBrokerClient1();
    initBrokerClient2();
  }

  private static void initBrokerClient1() throws IOException {
    BrokerClientImpl client = generator.createNewBrokerClient(getDefaultKey1());
    Properties properties = new Properties();
    properties.put("wildfly", "18");
    properties.put("postgresql", "14");
    properties.put("apache2", "2.4.41");
    client.putMyResourceProperties("software", properties);
  }

  private static void initBrokerClient2() throws IOException {
    BrokerClientImpl client = generator.createNewBrokerClient(getDefaultKey2());
    Properties properties = new Properties();
    properties.put("dwh-j2ee", "1.5.1");
    properties.put("dwh-api", "0.10");
    client.putMyResourceProperties("versions", properties);
  }

  @Order(1)
  @Test
  void getBrokerNodes() throws IOException {
    List<Node> nodes = adminClient.listNodes();
    Assertions.assertEquals(2, nodes.size());
  }

  @Order(2)
  @Test
  void getBrokerNode1() throws IOException {
    Node node = adminClient.getNode(0);
    Assertions.assertEquals("Schwarzwaldklinik", node.getCommonName());
  }

  @Order(3)
  @Test
  void getBrokerNode2() throws IOException {
    Node node = adminClient.getNode(1);
    Assertions.assertEquals("Plainsboro Teaching Hospital", node.getCommonName());
  }

  @Order(4)
  @Test
  void getNonExistingBrokerNode() throws IOException {
    Assertions.assertNull(adminClient.getNode(99));
  }

  @Order(5)
  @Test
  void getNodeProperties() throws IOException {
    Properties resource = adminClient.getNodeProperties(0, "software");
    Assertions.assertEquals(3, resource.size());
    Assertions.assertEquals("18", resource.get("wildfly"));
    Assertions.assertEquals("14", resource.get("postgresql"));
    Assertions.assertEquals("2.4.41", resource.get("apache2"));
  }

  @Order(6)
  @Test
  void getNodeString() throws IOException {
    String resource = adminClient.getNodeString(1, "versions");
    resource = resource.replaceAll("\n", "");
    Pattern regex = Pattern.compile("<properties>(.*?)</properties>");
    Matcher matcher = regex.matcher(resource);
    if (matcher.find()) {
      String propertiesSubstring = matcher.group(1);
      Assertions.assertEquals(
          "<comment>versions</comment><entry key=\"dwh-api\">0.10</entry><entry key=\"dwh-j2ee\">1.5.1</entry>",
          propertiesSubstring);
    } else {
      throw new IOException("No matching substring found");
    }
  }

  @Order(7)
  @Test
  void getNonExistingNodeResource() {
    // http code 404
    Assertions.assertThrows(InvalidPropertiesFormatException.class,
        () -> adminClient.getNodeProperties(0, "resources"));
  }
}
