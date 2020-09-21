package com.test.kakobank.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${server.msa-ap.external.connect-time-out}")
  int connectTimeOut;
  @Value("${server.msa-ap.external.read-time-out}")
  int readTimeOut;

  @Bean
  public WebClient webClient(){
    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().build();

    exchangeStrategies
        .messageWriters().stream()
        .filter(LoggingCodecSupport.class::isInstance)
        .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

    return WebClient.builder()
        .clientConnector(
            new ReactorClientHttpConnector(
                HttpClient
                    .create()
                    .tcpConfiguration(
                        client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOut)
                            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(readTimeOut)))
                    )
            )
        )
        .exchangeStrategies(exchangeStrategies)
        .filter(ExchangeFilterFunction.ofRequestProcessor(
            clientRequest -> {
              logger.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
              clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.debug("{} : {}", name, value)));
              return Mono.just(clientRequest);
            }
        ))
        .filter(ExchangeFilterFunction.ofResponseProcessor(
            clientResponse -> {
              clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> logger.debug("{} : {}", name, value)));
              return Mono.just(clientResponse);
            }
        ))
        .build();
  }

}
