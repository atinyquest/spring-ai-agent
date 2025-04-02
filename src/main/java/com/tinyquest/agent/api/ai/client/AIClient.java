package com.tinyquest.agent.api.ai.client;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface AIClient {
    String send(String requestBody);
    Flux<String> stream(Map<String, Object> requestBody);
}
