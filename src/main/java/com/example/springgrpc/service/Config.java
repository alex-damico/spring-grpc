package com.example.springgrpc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ResourceUtils;

@Slf4j
@Configuration
public class Config {

  @SneakyThrows
  private Map<String, ?> getRetryingServiceConfig() {
    File file = ResourceUtils.getFile("classpath:retrying_service_config.json");
    return new ObjectMapper().readValue(file, HashMap.class);
  }

  @Bean
  @Profile("client")
  public GrpcChannelConfigurer retryChannelConfigurer() {
//    log.info("Client started with retrying configuration: " + getRetryingServiceConfig());
    return (channelBuilder, name) -> {
      channelBuilder
//          .defaultServiceConfig(getRetryingServiceConfig())
          .enableRetry()
          .maxRetryAttempts(42);
    };
  }

}
