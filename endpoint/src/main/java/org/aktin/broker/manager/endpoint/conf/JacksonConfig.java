package org.aktin.broker.manager.endpoint.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.manager.endpoint.deserializer.NodeStatusInfoDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Autowired
  private NodeStatusInfoDeserializer nodeStatusInfoDeserializer;

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(NodeStatusInfo.class, nodeStatusInfoDeserializer);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(module);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }
}
