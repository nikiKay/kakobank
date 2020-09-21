package com.test.kakobank.config;

import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
  @Value("${server.msa-ap.internal.connect-time-out}")
  int connectTimeOut;
  @Value("${server.msa-ap.internal.read-time-out}")
  int readTimeOut;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
    return restTemplateBuilder
        .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
        .setConnectTimeout(Duration.ofMillis(connectTimeOut)) // connection-timeout
        .setReadTimeout(Duration.ofMillis(readTimeOut)) // read-timeout
        .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
        .build();
  }

}
