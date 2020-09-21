package com.test.kakobank;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KakobankApplication {
	Map<String, Object> requestSessions = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(KakobankApplication.class, args);
	}

	@Bean
	public Map<String, Object> lockRequestSession() {
		return requestSessions;
	}
}
