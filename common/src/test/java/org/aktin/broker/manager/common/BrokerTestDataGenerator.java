package org.aktin.broker.manager.common;

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

public class BrokerTestDataGenerator {

  private static URI BROKER_URI;
  private static final String DEFAULT_QUERYREQUEST_CONTENT_TYPE = "application/vnd.aktin.query.request+xml";

  public BrokerTestDataGenerator(String brokerUrl) {
    BROKER_URI = URI.create(brokerUrl);
  }

  public BrokerClientImpl getNewBrokerClient(String apiKey) {
    BrokerClientImpl client = new BrokerClientImpl(BROKER_URI);
    client.setClientAuthenticator(HttpApiKeyAuth.newBearer(apiKey));
    return client;
  }

  public String getDefaultQueryRequestContentType() {
    return DEFAULT_QUERYREQUEST_CONTENT_TYPE;
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
      JAXBContext jaxbContext = JAXBContext.newInstance(QueryRequest.class);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      StringWriter stringWriter = new StringWriter();
      marshaller.marshal(queryRequest, stringWriter);
      return stringWriter.toString();
    } catch (JAXBException e) {
      e.printStackTrace();
      return null;
    }
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
