package org.aktin.broker.manager.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client.BrokerClientImpl;
import org.aktin.broker.client.ResponseWithMetadata;
import org.aktin.broker.manager.BrokerContainer;
import org.aktin.broker.manager.BrokerTestDataGenerator;
import org.aktin.broker.manager.api.common.PropertiesFileResolver;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.aktin.broker.xml.RequestStatus;
import org.aktin.broker.xml.RequestStatusInfo;
import org.aktin.broker.xml.ResultInfo;
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

//TODO test aggregated results
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestBrokerResultConnection {

  private static BrokerTestDataGenerator generator;
  private static BrokerContainer broker;
  private static String brokerUrl;
  private static BrokerClientImpl client1;
  private static BrokerClientImpl client2;
  private static String defaultContentType;

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
    client1 = generator.getNewBrokerClient(broker.getDefaultKey1());
    client2 = generator.getNewBrokerClient(broker.getDefaultKey2());
    client1.getMyNode();
    client2.getMyNode();
    defaultContentType = generator.getDefaultQueryRequestContentType();
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
  void getEmptyResults() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(0));
    assertEmptyResultList(id);
    assertEmptyResultContent(id);
  }

  private void assertEmptyResultList(int id) throws IOException {
    List<ResultInfo> resultList = adminClient.listResults(id);
    Assertions.assertTrue(resultList.isEmpty());
  }

  private void assertEmptyResultContent(int id) throws IOException {
    ResponseWithMetadata result = adminClient.getResult(id, 0);
    Assertions.assertNull(result);
  }

  @Order(2)
  @Test
  void getResultAndResultString() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(1));
    letClient1And2CompleteRequest(id);
    assertResultList(id);
    assertNodeCompletionStatus(id);
    assertResultContent(id);
  }

  private void letClient1And2CompleteRequest(int id) throws IOException {
    client1.postRequestStatus(id, RequestStatus.completed);
    client1.putRequestResult(id, "text/csv", "a;b\n1;2\n3;4\n");
    client2.postRequestStatus(id, RequestStatus.completed);
    client2.putRequestResult(id, "text/csv", "a;b\n4;3\n2;1\n");
  }

  private void assertResultList(int id) throws IOException {
    List<ResultInfo> resultList = adminClient.listResults(id);
    Assertions.assertEquals(2, resultList.size());
  }

  private void assertNodeCompletionStatus(int id) throws IOException {
    List<RequestStatusInfo> requestStatusInfos = adminClient.listRequestStatus(id);
    for (RequestStatusInfo info : requestStatusInfos) {
      Assertions.assertNotNull(info.completed);
    }
  }

  private void assertResultContent(int id) throws IOException {
    ResponseWithMetadata result = adminClient.getResult(id, 0);
    String resultString = new String(result.getInputStream().readAllBytes(),
        StandardCharsets.UTF_8);
    Assertions.assertEquals("a;b\n1;2\n3;4\n", resultString);
    Assertions.assertEquals("text/csv;charset=UTF-8", result.getContentType());
    Assertions.assertEquals("_result_0", result.getName().substring(1));
  }

  @Order(3)
  @Test
  void deleteRequestAfterResults() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(1));
    letClient1And2CompleteRequest(id);
    assertResultContent(id);
    adminClient.deleteRequest(id);
    assertEmptyResultContent(id);
  }
}
