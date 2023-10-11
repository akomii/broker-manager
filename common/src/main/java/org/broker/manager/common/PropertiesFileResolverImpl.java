package org.broker.manager.common;

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
import org.broker.manager.api.common.PropertiesFileResolver;
import org.broker.manager.api.enums.PropertiesKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PropertiesFileResolverImpl implements PropertiesFileResolver {

  private final Logger logger = LoggerFactory.getLogger(PropertiesFileResolverImpl.class);

  private final Properties properties = new Properties();

  @PostConstruct
  public void init() {
    loadProperties();
    checkPropertiesFileForIntegrity();
  }

  private void loadProperties() {
    Path path = getPropertiesPath();
    try (Reader in = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      properties.load(in);
    } catch (IOException e) {
      logger.error("Error during reading of properties file", e);
    }
  }

  private Path getPropertiesPath() {
    String propertiesPath = System.getProperty("brokermanager.config.path");
    String defaultLinuxPath = "/etc/brokermanager/config.properties";
    return Paths.get(Objects.requireNonNullElse(propertiesPath, defaultLinuxPath));
  }

  private void checkPropertiesFileForIntegrity() throws IllegalStateException {
    if (!Arrays.stream(PropertiesKey.values())
        .allMatch(key -> properties.containsKey(key.toString()))) {
      throw new IllegalStateException("Properties file is missing some keys");
    }
  }

  public String getKeyValue(PropertiesKey key) {
    return properties.getProperty(key.toString());
  }
}
