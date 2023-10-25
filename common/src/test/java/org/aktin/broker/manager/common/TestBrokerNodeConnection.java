package org.aktin.broker.manager.common;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client.BrokerClientImpl;
import org.aktin.broker.manager.BrokerContainer;
import org.aktin.broker.manager.BrokerTestDataGenerator;
import org.aktin.broker.manager.api.common.PropertiesFileResolver;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.aktin.broker.xml.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestBrokerNodeConnection {

  private static BrokerTestDataGenerator generator;
  private static BrokerContainer broker;
  private static String brokerUrl;

  @Mock
  private PropertiesFileResolver propertiesFileResolver;

  @InjectMocks
  private BrokerAdminInitializerImpl brokerAdminInitializerImpl = new BrokerAdminInitializerImpl();

  private BrokerAdmin adminClient;

  @BeforeAll
  public static void init() throws IOException {
    broker = new BrokerContainer();
    brokerUrl = broker.getBrokerUrl();
    generator = new BrokerTestDataGenerator(brokerUrl);
    initBrokerClient1();
    initBrokerClient2();
  }

  private static void initBrokerClient1() throws IOException {
    BrokerClientImpl client = generator.getNewBrokerClient(broker.getDefaultKey1());
    Properties properties = new Properties();
    properties.put("wildfly", "18");
    properties.put("postgresql", "14");
    properties.put("apache2", "2.4.41");
    client.putMyResourceProperties("software", properties);
  }

  private static void initBrokerClient2() throws IOException {
    BrokerClientImpl client = generator.getNewBrokerClient(broker.getDefaultKey2());
    Properties properties = new Properties();
    properties.put("dwh-j2ee", "1.5.1");
    properties.put("dwh-api", "0.10");
    client.putMyResourceProperties("versions", properties);
  }

  @BeforeEach
  public void openMocks() {
    Mockito.when(propertiesFileResolver.getKeyValue(PropertiesKey.URL))
        .thenReturn(brokerUrl);
    Mockito.when(propertiesFileResolver.getKeyValue(PropertiesKey.APIKEY))
        .thenReturn(broker.getDefaultAdminKey());
    brokerAdminInitializerImpl.init();
    adminClient = brokerAdminInitializerImpl.getAdminClient();
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
  void getNonexistingBrokerNode() throws IOException {
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
  void getNonexistingNodeRessource() {
    // http code 404
    Assertions.assertThrows(InvalidPropertiesFormatException.class,
        () -> adminClient.getNodeProperties(0, "resources"));
  }
}
