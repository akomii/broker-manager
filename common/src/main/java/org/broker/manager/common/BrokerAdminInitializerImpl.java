package org.broker.manager.common;

import java.net.URI;
import org.aktin.broker.client2.BrokerAdmin2;
import org.aktin.broker.client2.auth.ApiKeyAuthentication;
import org.broker.manager.api.common.BrokerAdminInitializer;
import org.broker.manager.api.common.PropertiesFileResolver;
import org.broker.manager.api.enums.PropertiesKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerAdminInitializerImpl implements BrokerAdminInitializer {

  @Autowired
  private final PropertiesFileResolver propertiesFileResolver;

  private final BrokerAdmin2 brokerAdmin;

  @Autowired
  public BrokerAdminInitializerImpl(PropertiesFileResolver propertiesFileResolver) {
    this.propertiesFileResolver = propertiesFileResolver;
    URI brokerUri = URI.create(propertiesFileResolver.getKeyValue(PropertiesKey.URL));
    String apiKey = propertiesFileResolver.getKeyValue(PropertiesKey.APIKEY);
    this.brokerAdmin = createBrokerAdmin(brokerUri, apiKey);
  }

  private BrokerAdmin2 createBrokerAdmin(URI brokerUri, String apiKey) {
    BrokerAdmin2 admin = new BrokerAdmin2(brokerUri);
    admin.setAuthFilter(new ApiKeyAuthentication(apiKey));
    return admin;
  }

  public BrokerAdmin2 getAdminClient() {
    return brokerAdmin;
  }
}
