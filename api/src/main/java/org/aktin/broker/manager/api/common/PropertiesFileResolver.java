package org.aktin.broker.manager.api.common;

import org.aktin.broker.manager.api.enums.PropertiesKey;

public interface PropertiesFileResolver {

  String getKeyValue(PropertiesKey key);
}
