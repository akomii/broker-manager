import java.net.URL;
import org.broker.manager.api.enums.PropertiesKey;
import org.broker.manager.common.PropertiesFileResolverImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPropertiesFileResolverImpl {

  private final PropertiesFileResolverImpl propertiesFileResolver = new PropertiesFileResolverImpl();

  @Order(1)
  @Test
  public void getKeyValue() {
    System.setProperty("brokermanager.config.path", getTestResourcePath("good.properties"));
    propertiesFileResolver.init();
    Assertions.assertEquals("ABCD", propertiesFileResolver.getKeyValue(PropertiesKey.URL));
    Assertions.assertEquals("EFGH", propertiesFileResolver.getKeyValue(PropertiesKey.APIKEY));
  }

  private String getTestResourcePath(String resourceName) {
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(resourceName);
    assert resource != null;
    return resource.getPath();
  }

  @Order(2)
  @Test
  public void missingKeysInProperties() {
    System.setProperty("brokermanager.config.path", getTestResourcePath("bad.properties"));
    Assertions.assertThrows(IllegalStateException.class, propertiesFileResolver::init);
  }

  @Order(3)
  @Test
  public void noPropertiesFile() {
    System.setProperty("brokermanager.config.path", "missing.properties");
    Assertions.assertThrows(IllegalStateException.class, propertiesFileResolver::init);
  }
}
