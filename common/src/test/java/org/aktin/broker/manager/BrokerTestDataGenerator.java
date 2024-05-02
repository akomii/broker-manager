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

import java.io.StringWriter;
import java.net.URI;
import java.time.Instant;
import java.time.Period;
import java.util.Arrays;
import java.util.HashSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.aktin.broker.client.BrokerClientImpl;
import org.aktin.broker.client.auth.HttpApiKeyAuth;
import org.aktin.broker.query.xml.Principal;
import org.aktin.broker.query.xml.Query;
import org.aktin.broker.query.xml.QueryRequest;
import org.aktin.broker.query.xml.RepeatedExecution;
import org.aktin.broker.query.xml.SingleExecution;

/**
 * Utility class to generate test data for the Broker API. It provides methods for creating different types of {@link Query}, {@link QueryRequest},
 * and configuring broker clients, along with utility functions for marshalling data into XML format.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public class BrokerTestDataGenerator {

  private final URI brokerUri;

  public BrokerTestDataGenerator(String brokerUrl) {
    this.brokerUri = URI.create(brokerUrl);
  }

  public BrokerClientImpl createNewBrokerClient(String apiKey) {
    BrokerClientImpl client = new BrokerClientImpl(brokerUri);
    client.setClientAuthenticator(HttpApiKeyAuth.newBearer(apiKey));
    return client;
  }

  public String getDefaultQueryRequestContentType() {
    return "application/vnd.aktin.query.request+xml";
  }

  public String createDefaultSingleQueryRequest(int id) {
    Instant reference = Instant.parse("2022-01-01T00:00:00.00Z");
    Instant scheduled = Instant.parse("2022-02-02T00:00:00.00Z");
    Instant deadline = Instant.parse("2022-03-03T00:00:00.00Z");
    QueryRequest queryRequest = new QueryRequest(id, reference, scheduled, deadline,
        createDefaultSingleQuery());
    return convertQueryRequestToXmlString(queryRequest);
  }

  //TODO add Query Extensions
  public Query createDefaultSingleQuery() {
    Query query = new Query();
    query.title = "Default single query";
    query.description = "Tuesdays are free if you bring a gnome costume.";
    Principal principal = new Principal();
    principal.name = "Maddie Bennett";
    principal.organisation = "the testing department";
    principal.email = "m.bennett@randatmail.com";
    principal.phone = "118-6108-67";
    principal.tags = new HashSet<>(Arrays.asList("test", "single"));
    query.principal = principal;
    SingleExecution schedule = new SingleExecution();
    schedule.duration = Period.of(1, 2, 3);
    query.schedule = schedule;
    return query;
  }

  public String createDefaultSerialQueryRequest(int id) {
    Instant reference = Instant.parse("2022-04-04T00:00:00.00Z");
    Instant scheduled = Instant.parse("2022-05-05T00:00:00.00Z");
    Instant deadline = Instant.parse("2022-06-06T00:00:00.00Z");
    QueryRequest queryRequest = new QueryRequest(id, reference, scheduled, deadline,
        createDefaultSerialQuery());
    return convertQueryRequestToXmlString(queryRequest);
  }

  //TODO add Query Extensions
  public Query createDefaultSerialQuery() {
    Query query = new Query();
    query.title = "Default series query";
    query.description = "When transplanting seedlings, candied teapots will make the task easier.";
    Principal principal = new Principal();
    principal.name = "Ryan Gray";
    principal.organisation = "the other testing department";
    principal.email = "r.gray@randatmail.com";
    principal.phone = "843-7540-98";
    principal.tags = new HashSet<>(Arrays.asList("test", "series"));
    query.principal = principal;
    RepeatedExecution schedule = new RepeatedExecution();
    schedule.duration = Period.of(3, 2, 1);
    schedule.interval = Period.of(4, 4, 4);
    schedule.intervalHours = 4;
    schedule.id = 999;
    query.schedule = schedule;
    return query;
  }

  public String createEmptyQueryRequest() {
    QueryRequest queryRequest = new QueryRequest();
    return convertQueryRequestToXmlString(queryRequest);
  }

  private String convertQueryRequestToXmlString(QueryRequest queryRequest) {
    try {
      return marshalQueryRequest(queryRequest);
    } catch (JAXBException e) {
      throw new RuntimeException("Failed to marshal QueryRequest", e);
    }
  }

  private String marshalQueryRequest(QueryRequest queryRequest) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(QueryRequest.class);
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    StringWriter stringWriter = new StringWriter();
    marshaller.marshal(queryRequest, stringWriter);
    return stringWriter.toString();
  }

  public int[] getDefaultTargets1() {
    return new int[]{0, 1};
  }

  public int[] getDefaultTargets2() {
    return new int[]{0, 2};
  }

  public int[] getInvalidTargets() {
    return new int[]{666, 999};
  }
}
