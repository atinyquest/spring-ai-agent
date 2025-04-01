package com.tinyquest.exam.api.ai.client;

import com.tinyquest.exam.api.ai.property.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.tinyquest.exam.api.ai.model.constant.Constant.BEARER_PREFIX;

@Component
public class OpenAIClient implements AIClient {

    private final OpenAIProperties properties;
    private final WebClient webClient;

    public OpenAIClient(OpenAIProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + properties.getApiKey())
                .build();
    }

    @Override
    public String send(String requestBody) {
        return webClient.post()
                .uri(properties.getPath())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Flux<String> stream(Map<String, Object> requestBody) {
        return webClient.post()
                .uri(properties.getPath())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class);
    }
}
