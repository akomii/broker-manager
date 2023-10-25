package org.aktin.broker.manager.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestPropertiesFileResolverImpl {

  private final PropertiesFileResolverImpl propertiesFileResolver = new PropertiesFileResolverImpl();

  @Order(1)
  @Test
  void getKeyValue() throws IOException {
    System.setProperty("brokermanager.config.path", getTestResourcePath("good.properties"));
    propertiesFileResolver.init();
    Assertions.assertEquals("ABCD", propertiesFileResolver.getKeyValue(PropertiesKey.URL));
    Assertions.assertEquals("EFGH", propertiesFileResolver.getKeyValue(PropertiesKey.APIKEY));
  }

  private String getTestResourcePath(String resourceName) {
    ClassLoader classLoader = getClass().getClassLoader();
    URL resourceUrl = classLoader.getResource(resourceName);
    assert resourceUrl != null;
    File resource = new File(resourceUrl.getFile());
    return resource.getAbsolutePath();
  }

  @Order(2)
  @Test
  void missingKeysInProperties() {
    System.setProperty("brokermanager.config.path", getTestResourcePath("bad.properties"));
    Assertions.assertThrows(IllegalStateException.class, propertiesFileResolver::init);
  }

  @Order(3)
  @Test
  void noPropertiesFile() {
    System.setProperty("brokermanager.config.path", "missing.properties");
    Assertions.assertThrows(IllegalStateException.class, propertiesFileResolver::init);
  }
}
