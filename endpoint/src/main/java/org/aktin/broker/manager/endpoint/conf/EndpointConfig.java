package org.aktin.broker.manager.endpoint.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EndpointConfig implements WebMvcConfigurer {

  private static final String API_PATH_PREFIX = "/api";
  private static final String ENDPOINT_PACKAGE_NAME = "org.aktin.broker.manager.endpoint";

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix(API_PATH_PREFIX,
        controller -> controller.getPackageName().startsWith(ENDPOINT_PACKAGE_NAME));
  }
}
