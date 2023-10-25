package org.aktin.broker.manager.security.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

  IT(Code.IT),
  DAC(Code.DAC);

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

  /**
   * Inner class containing authority strings for user roles.
   */
  public static class Code {

    public static final String IT = "ROLE_IT";
    public static final String DAC = "ROLE_DAC";
  }
}
