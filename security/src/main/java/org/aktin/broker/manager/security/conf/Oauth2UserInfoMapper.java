package org.aktin.broker.manager.security.conf;

import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

@Configuration
public class Oauth2UserInfoMapper {

  @Value("${spring.security.oauth2.client.provider.broker-manager.authorities.roles}")
  private String rolesKeyChain;

  @Value("${spring.security.oauth2.client.provider.broker-manager.authorities.prefix}")
  private String rolesPrefix;

  private String[] rolesKeys;

  @PostConstruct
  public void init() {
    rolesKeys = rolesKeyChain.split("\\.");
  }

  @Bean
  public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.stream()
          .filter(OidcUserAuthority.class::isInstance)
          .map(OidcUserAuthority.class::cast)
          .map(OidcUserAuthority::getUserInfo)
          .map(userInfo -> getNestedListForKey(userInfo.getClaims(), rolesKeys))
          .map(roles -> (Collection<String>) roles)
          .forEach(roles -> mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles)));
      return mappedAuthorities;
    };
  }

  // Retrieves a nested list of values from a hierarchical map using a sequence of keys.
  private List<String> getNestedListForKey(Map<String, Object> map, String[] keys) {
    Object value = map;
    // Ensure that the keys array is not empty.
    if (keys.length == 0) {
      throw new IllegalArgumentException("The keys array cannot be empty.");
    }
    // Traverse the map hierarchy using the keys.
    for (String key : keys) {
      if (value instanceof Map) {
        value = ((Map<?, ?>) value).get(key);
        if (value == null) {
          throw new IllegalArgumentException(String.format("Key '%s' not found in the map.", key));
        }
      } else {
        throw new IllegalArgumentException(
            String.format("Key '%s' annot be accessed because it's not a map.", key));
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

  private List<SimpleGrantedAuthority> generateAuthoritiesFromClaim(Collection<String> claim) {
    return claim.stream()
        .map(role -> new SimpleGrantedAuthority(rolesPrefix + role))
        .toList();
  }
}
