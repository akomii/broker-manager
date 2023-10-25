package org.aktin.broker.manager.common;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client2.BrokerAdmin2;
import org.aktin.broker.client2.auth.ApiKeyAuthentication;
import org.aktin.broker.manager.api.common.BrokerAdminInitializer;
import org.aktin.broker.manager.api.common.PropertiesFileResolver;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerAdminInitializerImpl implements BrokerAdminInitializer {

  private static final Logger log = LoggerFactory.getLogger(BrokerAdminInitializerImpl.class);

  @Autowired
  private PropertiesFileResolver propertiesFileResolver;

  private BrokerAdmin brokerAdmin;

  @PostConstruct
  public void init() {
    this.brokerAdmin = initBrokerAdmin();
  }

  private BrokerAdmin initBrokerAdmin() {
    String uriString = propertiesFileResolver.getKeyValue(PropertiesKey.URL);
    String apiKey = propertiesFileResolver.getKeyValue(PropertiesKey.APIKEY);
    if (uriString == null || apiKey == null) {
      throw new IllegalStateException("Broker URI or API key is null. Initialization failed");
    }
    URI brokerUri = URI.create(uriString);
    log.info("Initializing BrokerAdmin with URI: {}", brokerUri);
    BrokerAdmin2 admin = new BrokerAdmin2(brokerUri);
    admin.setAuthFilter(new ApiKeyAuthentication(apiKey));
    return admin;
  }

  public BrokerAdmin getAdminClient() {
    return brokerAdmin;
  }
}
