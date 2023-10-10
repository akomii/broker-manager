import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractBrokerContainer {

  protected static final GenericContainer BROKER;
  protected static final String BROKER_URL;
  protected static final String DEFAULT_ADMIN_KEY = "xxxAdmin1234";
  protected static final String DEFAULT_KEY_1 = "xxxApiKey123";
  protected static final String DEFAULT_KEY_2 = "xxxApiKey567";
  protected static final String DEFAULT_KEY_3 = "xxxApiKey890";

  static {
    BROKER = new GenericContainer(DockerImageName.parse("ghcr.io/aktin/aktin-broker:latest"))
        .withExposedPorts(8080)
        .waitingFor(Wait.forHttp("/broker/status").forStatusCode(200))
        .withStartupTimeout(Duration.ofSeconds(20))
        .withReuse(true);
    BROKER.start();
    URIBuilder builder = new URIBuilder();
    builder.setScheme("http").setHost(BROKER.getHost()).setPort(BROKER.getMappedPort(8080))
        .setPath("broker/");
    BROKER_URL = builder.toString();
  }
}
