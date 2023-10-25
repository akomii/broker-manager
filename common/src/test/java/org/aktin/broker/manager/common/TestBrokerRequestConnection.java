package org.aktin.broker.manager.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client.BrokerClientImpl;
import org.aktin.broker.manager.BrokerContainer;
import org.aktin.broker.manager.BrokerTestDataGenerator;
import org.aktin.broker.manager.api.common.PropertiesFileResolver;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.aktin.broker.xml.RequestInfo;
import org.aktin.broker.xml.RequestStatus;
import org.aktin.broker.xml.RequestStatusInfo;
import org.aktin.broker.xml.util.Util;
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
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestBrokerRequestConnection {

  private static BrokerTestDataGenerator generator;
  private static BrokerContainer broker;
  private static String brokerUrl;
  private static BrokerClientImpl client1;
  private static BrokerClientImpl client2;
  private static BrokerClientImpl client3;
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
    client3 = generator.getNewBrokerClient(broker.getDefaultKey3());
    client1.getMyNode();
    client2.getMyNode();
    client3.getMyNode();
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
  void checkEmptyBrokerRequests() throws IOException {
    List<RequestInfo> requests = adminClient.listAllRequests();
    Assertions.assertTrue(requests.isEmpty());
  }

  @Order(2)
  @Test
  void createSingleRequestAndUpdateToSerial()
      throws IOException, ParserConfigurationException, SAXException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(0));
    checkBrokerRequestSize(1);
    adminClient.setRequestTargetNodes(id, generator.getDefaultTargets1());
    getRequestFromServerAndCheckAgainst(id, generator.createDefaultSingleQueryRequest(id));
    getTargetsFromServerAndCheckAgainst(id, generator.getDefaultTargets1());
    adminClient.putRequestDefinition(id, defaultContentType,
        generator.createDefaultSerialQueryRequest(0));
    adminClient.setRequestTargetNodes(id, generator.getDefaultTargets2());
    getRequestFromServerAndCheckAgainst(id, generator.createDefaultSerialQueryRequest(id));
    getTargetsFromServerAndCheckAgainst(id, generator.getDefaultTargets2());
    checkBrokerRequestSize(1);
  }

  private void checkBrokerRequestSize(int expected) throws IOException {
    List<RequestInfo> requests = adminClient.listAllRequests();
    Assertions.assertEquals(expected, requests.size());
  }

  //TODO check for Query Extensions
  private void getRequestFromServerAndCheckAgainst(int id, String expected)
      throws IOException, ParserConfigurationException, SAXException {
    Reader reader = adminClient.getRequestDefinition(id, defaultContentType);
    String actual = Util.readContent(reader);
    assertRequestStrings(expected, actual);
  }

  private void assertRequestStrings(String expected, String actual)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document expectedDoc = builder.parse(new InputSource(new StringReader(expected)));
    Document actualDoc = builder.parse(new InputSource(new StringReader(actual)));
    Assertions.assertTrue(expectedDoc.isEqualNode(actualDoc));
  }

  private void getTargetsFromServerAndCheckAgainst(int id, int[] expected) throws IOException {
    int[] actual = adminClient.getRequestTargetNodes(id);
    Assertions.assertArrayEquals(expected, actual);
  }

  @Order(3)
  @Test
  void createSerialRequestAndDeleteIt() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSerialQueryRequest(1));
    checkBrokerRequestSize(2);
    adminClient.deleteRequest(id);
    checkBrokerRequestSize(1);
  }

  @Order(4)
  @Test
  void getNonexistingBrokerRequest() throws IOException {
    Assertions.assertNull(adminClient.getRequestDefinition(111, defaultContentType));
  }

  @Order(5)
  @Test
  void deleteNonexistingBrokerRequest() throws IOException {
    adminClient.deleteRequest(333);
  }

  @Order(6)
  @Test
  void addRequestDefinitionToNonexistingRequest() throws IOException {
    adminClient.putRequestDefinition(222, defaultContentType,
        generator.createDefaultSingleQueryRequest(222));
  }

  @Order(7)
  @Test
  void getTargetNodesOfNonexistingRequest() throws IOException {
    Assertions.assertNull(adminClient.getRequestTargetNodes(555));
  }

  @Order(8)
  @Test
  void addTargetNodesToNonexistingRequest() throws IOException {
    int[] targetNodes = generator.getDefaultTargets1();
    adminClient.setRequestTargetNodes(444, targetNodes);
  }

  @Order(9)
  @Test
  void deleteTargetNodesFromNonexistingRequest() {
    Assertions.assertThrows(IOException.class, () -> adminClient.clearRequestTargetNodes(666));
  }

  @Order(10)
  @Test
  void publishRequestAndUpdateItsContent()
      throws IOException, ParserConfigurationException, SAXException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(2));
    adminClient.setRequestTargetNodes(id, generator.getDefaultTargets1());
    adminClient.publishRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
    letNodesRetrieveRequest(id);
    assertRequestContentOfTargetedNodes(id, generator.createDefaultSingleQueryRequest(id));
    // update step
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> adminClient.putRequestDefinition(id, generator.createEmptyQueryRequest(),
            defaultContentType));
  }

  private void checkAvailableRequestsOfNodes(int expected1, int expected2, int expected3) {
    try {
      Assertions.assertEquals(expected1, client1.listMyRequests().size());
      Assertions.assertEquals(expected2, client2.listMyRequests().size());
      Assertions.assertEquals(expected3, client3.listMyRequests().size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void letNodesRetrieveRequest(int id) throws IOException {
    int[] nodes;
    RequestInfo info = adminClient.getRequestInfo(id);
    if (info.targeted) {
      nodes = adminClient.getRequestTargetNodes(id);
    } else {
      nodes = new int[]{0, 1, 2};
    }
    for (int nodeId : nodes) {
      switch (nodeId) {
        case 0 -> client1.postRequestStatus(id, RequestStatus.retrieved);
        case 1 -> client2.postRequestStatus(id, RequestStatus.retrieved);
        case 2 -> client3.postRequestStatus(id, RequestStatus.retrieved);
      }
    }
  }

  private void assertRequestContentOfTargetedNodes(int id, String expected)
      throws IOException, ParserConfigurationException, SAXException {
    int[] nodes;
    RequestInfo info = adminClient.getRequestInfo(id);
    if (info.targeted) {
      nodes = adminClient.getRequestTargetNodes(id);
    } else {
      nodes = new int[]{0, 1, 2};
    }
    for (int nodeId : nodes) {
      String request = null;
      switch (nodeId) {
        case 0 -> request = client1.getMyRequestDefinitionString(id,
            defaultContentType);
        case 1 -> request = client2.getMyRequestDefinitionString(id,
            defaultContentType);
        case 2 -> request = client3.getMyRequestDefinitionString(id,
            defaultContentType);
      }
      assertRequestStrings(expected, request);
    }
  }

  @Order(11)
  @Test
  void publishEmptyRequestAndDeleteIt() throws IOException {
    int id = adminClient.createRequest();
    adminClient.publishRequest(id);
    checkAvailableRequestsOfNodes(2, 2, 1);
    letNodesRetrieveRequest(id);
    adminClient.deleteRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
  }

  @Order(12)
  @Test
  void publishedEmptyRequestAnDeleteItBeforeRetrieval() throws IOException {
    int id = adminClient.createRequest();
    adminClient.publishRequest(id);
    adminClient.deleteRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
  }

  @Order(13)
  @Test
  void publishRequestWithInvalidTargets() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(3));
    adminClient.setRequestTargetNodes(id, generator.getInvalidTargets());
    adminClient.publishRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
  }

  @Order(14)
  @Test
  void publishNonexistingRequest() {
    Assertions.assertThrows(IOException.class, () -> adminClient.publishRequest(777));
  }

  @Order(15)
  @Test
  void closeRequestAndPublishAgain() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(4));
    adminClient.publishRequest(id);
    checkAvailableRequestsOfNodes(2, 2, 1);
    letNodesRetrieveRequest(id);
    adminClient.closeRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
    adminClient.publishRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
  }

  @Order(16)
  @Test
  void closeUnpublishedRequestAndCloseItAgain() throws IOException {
    int id = adminClient.createRequest();
    adminClient.closeRequest(id);
    adminClient.closeRequest(id);
  }

  @Order(17)
  @Test
  void publishAClosedRequest() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(5));
    adminClient.closeRequest(id);
    adminClient.publishRequest(id);
    checkAvailableRequestsOfNodes(1, 1, 0);
  }

  @Order(18)
  @Test
  void closeNonexistingRequest() {
    Assertions.assertThrows(IOException.class, () -> adminClient.closeRequest(999));
  }

  @Order(19)
  @Test
  void getRequestInfo() throws IOException {
    int id = adminClient.createRequest(defaultContentType,
        generator.createDefaultSingleQueryRequest(6));
    assertEmptyRequestInfo(id);
    adminClient.setRequestTargetNodes(id, generator.getDefaultTargets1());
    assertRequestInfoBeforePublishing(id);
    adminClient.publishRequest(id);
    letNodesRetrieveRequest(id);
    assertRequestInfoAfterPublishingAndRetrieval(id);
    adminClient.closeRequest(id);
    assertRequestInfoAfterClosing(id);
  }

  private void assertEmptyRequestInfo(int id) throws IOException {
    RequestInfo info = adminClient.getRequestInfo(id);
    Assertions.assertEquals("[" + generator.getDefaultQueryRequestContentType() + "]",
        Arrays.toString(info.types));
    Assertions.assertFalse(info.targeted);
    Assertions.assertNull(info.published);
    Assertions.assertNull(info.closed);
    Assertions.assertNull(info.nodeStatus);
  }

  private void assertRequestInfoBeforePublishing(int id) throws IOException {
    RequestInfo info = adminClient.getRequestInfo(id);
    Assertions.assertEquals("[" + generator.getDefaultQueryRequestContentType() + "]",
        Arrays.toString(info.types));
    Assertions.assertTrue(info.targeted);
    Assertions.assertNull(info.published);
    Assertions.assertNull(info.closed);
    Assertions.assertNull(info.nodeStatus);
  }

  private void assertRequestInfoAfterPublishingAndRetrieval(int id) throws IOException {
    RequestInfo info = adminClient.getRequestInfo(id);
    Assertions.assertEquals("[" + generator.getDefaultQueryRequestContentType() + "]",
        Arrays.toString(info.types));
    Assertions.assertTrue(info.targeted);
    Assertions.assertNotNull(info.published);
    Assertions.assertNull(info.closed);
    Assertions.assertNull(info.nodeStatus);
  }

  private void assertRequestInfoAfterClosing(int id) throws IOException {
    RequestInfo info = adminClient.getRequestInfo(id);
    Assertions.assertEquals("[" + generator.getDefaultQueryRequestContentType() + "]",
        Arrays.toString(info.types));
    Assertions.assertTrue(info.targeted);
    Assertions.assertNotNull(info.published);
    Assertions.assertNotNull(info.closed);
    Assertions.assertNull(info.nodeStatus);
  }

  @Order(20)
  @Test
  void getRequestInfoOfNonexistingRequest() throws IOException {
    Assertions.assertNull(adminClient.getRequestInfo(999));
  }

  @Order(21)
  @Test
  void getRequestStatus() throws IOException {
    int id = adminClient.createRequest();
    assertEmptyRequestStatus(id);
    adminClient.putRequestDefinition(id, defaultContentType,
        generator.createDefaultSingleQueryRequest(id));
    adminClient.setRequestTargetNodes(id, generator.getDefaultTargets1());
    assertRequestStatusBeforePublishing(id);
    adminClient.publishRequest(id);
    letNodesRetrieveRequest(id);
    assertRequestStatusAfterPublishingAndRetrieval(id);
    adminClient.closeRequest(id);
    letNodesRetrieveRequest(id);
    assertRequestStatusAfterClosing(id);
  }

  private void assertEmptyRequestStatus(int id) throws IOException {
    List<RequestStatusInfo> requestStatusList = adminClient.listRequestStatus(id);
    Assertions.assertNotNull(requestStatusList);
  }

  private void assertRequestStatusBeforePublishing(int id) throws IOException {
    List<RequestStatusInfo> requestStatusList = adminClient.listRequestStatus(id);
    for (RequestStatusInfo info : requestStatusList) {
      Assertions.assertNull(info.deleted);
      Assertions.assertNull(info.retrieved);
      Assertions.assertNull(info.queued);
      Assertions.assertNull(info.processing);
      Assertions.assertNull(info.completed);
      Assertions.assertNull(info.rejected);
      Assertions.assertNull(info.failed);
      Assertions.assertNull(info.expired);
      Assertions.assertNull(info.interaction);
      Assertions.assertNull(info.type);
    }
  }

  private void assertRequestStatusAfterPublishingAndRetrieval(int id) throws IOException {
    List<RequestStatusInfo> requestStatusList = adminClient.listRequestStatus(id);
    for (RequestStatusInfo info : requestStatusList) {
      Assertions.assertNull(info.deleted);
      Assertions.assertNotNull(info.retrieved);
      Assertions.assertNull(info.queued);
      Assertions.assertNull(info.processing);
      Assertions.assertNull(info.completed);
      Assertions.assertNull(info.rejected);
      Assertions.assertNull(info.failed);
      Assertions.assertNull(info.expired);
      Assertions.assertNull(info.interaction);
      Assertions.assertNull(info.type);
    }
  }

  private void assertRequestStatusAfterClosing(int id) throws IOException {
    List<RequestStatusInfo> requestStatusList = adminClient.listRequestStatus(id);
    for (RequestStatusInfo info : requestStatusList) {
      Assertions.assertNull(info.deleted);
      Assertions.assertNotNull(info.retrieved);
      Assertions.assertNull(info.queued);
      Assertions.assertNull(info.processing);
      Assertions.assertNull(info.completed);
      Assertions.assertNull(info.rejected);
      Assertions.assertNull(info.failed);
      Assertions.assertNull(info.expired);
      Assertions.assertNull(info.interaction);
      Assertions.assertNull(info.type);
    }
  }

  @Order(22)
  @Test
  void getRequestStatusOfNonexistingRequest() throws IOException {
    assertEmptyRequestStatus(999);
  }

  @Order(23)
  @Test
  void getRequestNodeMesage() throws IOException {
    int id = adminClient.createRequest();
    adminClient.putRequestDefinition(id, defaultContentType,
        generator.createDefaultSingleQueryRequest(id));
    String msg = "I have retrieved it";
    client1.postRequestStatus(id, RequestStatus.retrieved, Instant.now(), msg);
    Reader reader = adminClient.getRequestNodeMessage(id, 0);
    Assertions.assertEquals(msg, readerToString(reader));
  }

  private String readerToString(Reader reader) throws IOException {
    StringWriter writer = new StringWriter();
    FileCopyUtils.copy(reader, writer);
    return writer.toString();
  }
}
