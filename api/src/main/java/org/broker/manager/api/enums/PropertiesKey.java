package org.broker.manager.api.enums;

/**
 * Possible keys for properties file of broker-manager.
 */
public enum PropertiesKey {

  URL("broker.url"),

  APIKEY("broker.apikey");

  private final String value;

  PropertiesKey(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
