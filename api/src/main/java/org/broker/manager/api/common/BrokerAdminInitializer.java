package org.broker.manager.api.common;

import org.aktin.broker.client2.BrokerAdmin2;

public interface BrokerAdminInitializer {

  BrokerAdmin2 getAdminClient();
}
