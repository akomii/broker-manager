package org.aktin.broker.manager.common;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import org.aktin.broker.client.BrokerAdmin;
import org.aktin.broker.client2.BrokerAdmin2;
import org.aktin.broker.client2.auth.ApiKeyAuthentication;
import org.aktin.broker.manager.api.common.BrokerAdminInitializer;
import org.aktin.broker.manager.api.common.PropertiesFileResolver;
import org.aktin.broker.manager.api.enums.PropertiesKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerAdminInitializerImpl implements BrokerAdminInitializer {

  @Autowired
  private PropertiesFileResolver propertiesFileResolver;

  private BrokerAdmin brokerAdmin;

  @PostConstruct
  public void init() {
    URI brokerUri = URI.create(propertiesFileResolver.getKeyValue(PropertiesKey.URL));
    String apiKey = propertiesFileResolver.getKeyValue(PropertiesKey.APIKEY);
    this.brokerAdmin = createBrokerAdmin(brokerUri, apiKey);
  }

  private BrokerAdmin createBrokerAdmin(URI brokerUri, String apiKey) {
    BrokerAdmin2 admin = new BrokerAdmin2(brokerUri);
    admin.setAuthFilter(new ApiKeyAuthentication(apiKey));
    return admin;
  }

  public BrokerAdmin getAdminClient() {
    return brokerAdmin;
  }
}
