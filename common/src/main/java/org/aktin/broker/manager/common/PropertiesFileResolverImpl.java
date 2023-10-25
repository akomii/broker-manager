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

@Service
public class PropertiesFileResolverImpl implements PropertiesFileResolver {

  private static final Logger log = LoggerFactory.getLogger(PropertiesFileResolverImpl.class);

  private final Properties properties = new Properties();

  @Value("${broker-manager.config.default-linux-path}")
  private String defaultLinuxPath;

  @PostConstruct
  public void init() {
    try {
      loadProperties();
      checkPropertiesFileForIntegrity();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load properties", e);
    }
  }

  private void loadProperties() throws IOException {
    Path path = getPropertiesPath();
    if (Files.exists(path)) {
      try (Reader in = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        properties.load(in);
      }
    } else {
      throw new IllegalStateException(String.format("Properties file not found at %s", path));
    }
  }

  private Path getPropertiesPath() {
    String propertiesPath = System.getProperty("brokermanager.config.path");
    if (propertiesPath == null) {
      log.info(
          "System property 'brokerbrokermanager.config.path' is null. Using default Linux path: {}",
          defaultLinuxPath);
    }
    return Paths.get(Objects.requireNonNullElse(propertiesPath, defaultLinuxPath));
  }

  private void checkPropertiesFileForIntegrity() {
    boolean allKeysPresent = Arrays.stream(PropertiesKey.values())
        .allMatch(key -> properties.containsKey(key.toString()));
    if (!allKeysPresent) {
      throw new IllegalStateException("Properties file is missing some keys");
    }
  }

  public String getKeyValue(PropertiesKey key) {
    String value = properties.getProperty(key.toString());
    if (value == null) {
      log.warn("Key {} not found in properties", key);
    }
    return value;
  }
}
