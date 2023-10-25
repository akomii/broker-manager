package org.aktin.broker.manager.security.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  @Autowired
  private JwtUserRoleConverter jwtUserRoleConverter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(authorize ->
        authorize.anyRequest().authenticated());
    http.oauth2ResourceServer(oauth2 ->
        oauth2.jwt(jwtConfigurer ->
            jwtConfigurer.jwtAuthenticationConverter(jwtUserRoleConverter)));
    return http.build();
  }
}
