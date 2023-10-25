package org.aktin.broker.manager;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class BrokerContainer {

  private final GenericContainer broker;
  private final String brokerUrl;

  public BrokerContainer() {
    this.broker = initBrokerContainer();
    this.brokerUrl = initBrokerUrl();
  }

  private GenericContainer initBrokerContainer() {
    GenericContainer container = new GenericContainer(
        DockerImageName.parse("ghcr.io/aktin/aktin-broker:latest"))
        .withExposedPorts(8080)
        .waitingFor(Wait.forHttp("/broker/status").forStatusCode(200))
        .withStartupTimeout(Duration.ofSeconds(20))
        .withReuse(true);
    container.start();
    return container;
  }

  private String initBrokerUrl() {
    URIBuilder builder = new URIBuilder();
    builder
        .setScheme("http")
        .setHost(broker.getHost())
        .setPort(broker.getMappedPort(8080))
        .setPath("broker/");
    return builder.toString();
  }

  public String getBrokerUrl() {
    return brokerUrl;
  }

  public String getDefaultAdminKey() {
    return "xxxAdmin1234";
  }

  public String getDefaultKey1() {
    return "xxxApiKey123";
  }

  public String getDefaultKey2() {
    return "xxxApiKey567";
  }

  public String getDefaultKey3() {
    return "xxxApiKey890";
  }
}
