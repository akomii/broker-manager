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
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.aktin.broker.client.BrokerClientImpl;
import org.aktin.broker.client.ResponseWithMetadata;
import org.aktin.broker.xml.RequestStatus;
import org.aktin.broker.xml.RequestStatusInfo;
import org.aktin.broker.xml.ResultInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Provides tests for the retrieval and management of AKTIN Broker request results. Includes the following specific test cases:
 * <ul>
 *   <li>Handling empty result scenarios.</li>
 *   <li>Retrieving single results as both metadata and content.</li>
 *   <li>Verifying result lists for completed requests.</li>
 *   <li>Ensuring result deletion upon associated request deletion.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
//TODO test aggregated results
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrokerResultTest extends AbstractBrokerConnectionTest {

  private static BrokerClientImpl client1;
  private static BrokerClientImpl client2;
  private static String defaultContentType;

  @BeforeAll
  public static void init() throws IOException {
    client1 = generator.createNewBrokerClient(getDefaultKey1());
    client2 = generator.createNewBrokerClient(getDefaultKey2());
    client1.getMyNode();
    client2.getMyNode();
    defaultContentType = generator.getDefaultQueryRequestContentType();
  }

  @Order(1)
  @Test
  void getEmptyResults() throws IOException {
    int id = adminClient.createRequest(defaultContentType, generator.createDefaultSingleQueryRequest(0));
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
    int id = adminClient.createRequest(defaultContentType, generator.createDefaultSingleQueryRequest(1));
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
    String resultString = new String(result.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    Assertions.assertEquals("a;b\n1;2\n3;4\n", resultString);
    Assertions.assertEquals("text/csv;charset=UTF-8", result.getContentType());
    Assertions.assertEquals("_result_0", result.getName().substring(1));
  }

  @Order(3)
  @Test
  void deleteRequestAfterResults() throws IOException {
    int id = adminClient.createRequest(defaultContentType, generator.createDefaultSingleQueryRequest(1));
    letClient1And2CompleteRequest(id);
    assertResultContent(id);
    adminClient.deleteRequest(id);
    assertEmptyResultContent(id);
  }
}
