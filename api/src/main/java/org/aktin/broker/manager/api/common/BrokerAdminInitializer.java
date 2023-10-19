package org.aktin.broker.manager.api.common;

import org.aktin.broker.client.BrokerAdmin;

public interface BrokerAdminInitializer {

  BrokerAdmin getAdminClient();
}
