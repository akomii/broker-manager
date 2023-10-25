package org.aktin.broker.manager.security.conf;

import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class JwtUserRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  @Value("${spring.security.oauth2.resourceserver.authorities.roles}")
  private String rolesKeyChain;

  @Value("${spring.security.oauth2.resourceserver.authorities.prefix}")
  private String rolesPrefix;

  private String[] rolesKeys;

  @PostConstruct
  public void init() {
    rolesKeys = rolesKeyChain.split("\\.");
  }

  public AbstractAuthenticationToken convert(Jwt jwt) {
    Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
    return new JwtAuthenticationToken(jwt, authorities);
  }

  private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
    Map<String, Object> claims = jwt.getClaims();
    Collection<String> roles = extractRolesFromClaims(claims);
    return roles.stream().map(role ->
        new SimpleGrantedAuthority(rolesPrefix + role)).collect(Collectors.toSet());
  }

  private Collection<String> extractRolesFromClaims(Map<String, Object> claims) {
    Object value = claims;
    // Traverse the map hierarchy using the keys.
    for (String key : rolesKeys) {
      if (value instanceof Map) {
        value = ((Map<?, ?>) value).get(key);
        if (value == null) {
          throw new IllegalArgumentException(String.format("Key '%s' not found in claims.", key));
        }
      } else {
        throw new IllegalArgumentException("Cannot traverse claims because it's not a map.");
      }
    }
    // Check if the final value is a list and return it.
    if (value instanceof List) {
      return (List<String>) value;
    } else {
      throw new IllegalArgumentException(
          "Last key should contain a list or array of strings, but it does not.");
    }
  }
}
