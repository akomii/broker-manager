package org.broker.manager.api.common;

import org.broker.manager.api.enums.PropertiesKey;

public interface PropertiesFileResolver {
  
  String getKeyValue(PropertiesKey key);
}
