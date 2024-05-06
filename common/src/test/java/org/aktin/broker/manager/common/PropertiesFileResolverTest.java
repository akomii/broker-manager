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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Provides tests for retrieving key values from properties files using the {@link PropertiesFileResolverImpl}. This includes tests for:
 * <ul>
 *   <li>Successfully retrieving values.</li>
 *   <li>Handling cases of missing keys.</li>
 *   <li>Handling cases of missing properties file.</li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PropertiesFileResolverTest {

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
