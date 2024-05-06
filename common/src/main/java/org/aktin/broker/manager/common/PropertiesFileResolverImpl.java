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

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import org.aktin.broker.manager.api.common.PropertiesFileResolver;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service responsible for loading, validating, and accessing configuration properties stored in a properties file.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
@Service
public class PropertiesFileResolverImpl implements PropertiesFileResolver {

  private static final Logger log = LoggerFactory.getLogger(PropertiesFileResolverImpl.class);

  private final Properties properties = new Properties();

  /**
   * Path to the default properties file under Linux systems, injected from application configuration (application-common.yml).
   */
  @Value("${broker-manager.config.default-linux-path}")
  private String defaultLinuxPath;

  /**
   * Loads properties from file and performs initial validation checks.
   *
   * @throws IllegalStateException If properties loading or validation fails.
   */
  @PostConstruct
  public void init() {
    try {
      Path path = getPropertiesPath();
      loadPropertiesFromFile(path);
      checkPropertiesFileForIntegrity();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load properties", e);
    }
  }

  /**
   * Determines the path to the properties file. Prioritizes a system property (`brokermanager.config.path`) and falls back to a default
   * (`defaultLinuxPath`) if the property is not set.
   *
   * @return Path object representing the location of the properties file.
   */
  private Path getPropertiesPath() {
    String propertiesPath = System.getProperty("brokermanager.config.path");
    if (propertiesPath == null) {
      log.info("System property 'brokermanager.config.path' is null. Using default Linux path: {}", defaultLinuxPath);
    }
    return Paths.get(Objects.requireNonNullElse(propertiesPath, defaultLinuxPath));
  }

  /**
   * Loads and parses the properties file from the determined location.
   *
   * @param path Path object representing the file location.
   * @throws IOException           If errors occur when reading the properties file.
   * @throws IllegalStateException If Path to properties file is invalid.
   */
  private void loadPropertiesFromFile(Path path) throws IOException {
    if (Files.exists(path)) {
      try (Reader in = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        properties.load(in);
      }
    } else {
      throw new IllegalStateException(String.format("Properties file not found at %s", path));
    }
  }

  /**
   * Verifies that the loaded properties file contains all required keys (as defined in the PropertiesKey enum).
   *
   * @throws IllegalStateException If any keys are missing in properties file.
   */
  private void checkPropertiesFileForIntegrity() throws IllegalStateException {
    boolean allKeysPresent = Arrays.stream(PropertiesKey.values()).allMatch(key -> properties.containsKey(key.toString()));
    if (!allKeysPresent) {
      throw new IllegalStateException("Properties file is missing some keys");
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getKeyValue(PropertiesKey key) {
    String value = properties.getProperty(key.toString());
    if (value == null) {
      log.warn("Key '{}' not found in properties", key);
    }
    return value;
  }
}
