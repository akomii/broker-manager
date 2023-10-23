package org.aktin.broker.manager.security.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private static final String ERROR_PATH = "/error";
  private static final String LOGIN_PATH = "/login";
  private static final String JSESSIONID_COOKIE = "JSESSIONID";

  @Value("${spring.security.oauth2.client.provider.broker-manager.logout-url}")
  private String logoutUrl;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers(ERROR_PATH, LOGIN_PATH)
        .permitAll()
        .anyRequest()
        .authenticated());
    http.oauth2Login(Customizer.withDefaults());
    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    http.logout(
        logout -> logout
            .logoutSuccessHandler(
                (request, response, authentication) -> response.sendRedirect(logoutUrl))
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies(JSESSIONID_COOKIE));
    return http.build();
  }
}
