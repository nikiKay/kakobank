package com.test.kakobank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Service
public class ApiService {

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  WebClient webClient;

  public ResponseEntity<Map> sendByPost(String url, @Nullable MultiValueMap headers, Map<String, Object> params) {
    String body = null;
    try {
      body = new ObjectMapper().writeValueAsString(params);
    } catch (JsonProcessingException e) {
      throw new RuntimeException();
    }

    HttpHeaders httpHeaders = new HttpHeaders(headers);
    HttpEntity httpEntity = new HttpEntity(body, httpHeaders);
    ResponseEntity<Map> responseMap = restTemplate.postForEntity(url, httpEntity, Map.class);

    return responseMap;
  }

  public ResponseEntity<Map> sendByPost(String url, Map<String, Object> params) throws RuntimeException {
    return this.sendByPost(url, null, params);
  }

  public Flux<String> sendByPostWebClient(String url, String uri, Map<String, Object> params) throws RuntimeException {
    return webClient.mutate()
        .baseUrl(url)
        .build()
        .post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(params)
        .retrieve()
        .bodyToMono(String.class)
        .flux();
  }
}
