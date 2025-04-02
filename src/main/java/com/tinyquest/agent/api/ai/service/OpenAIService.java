package com.tinyquest.agent.api.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyquest.agent.api.ai.client.AIClient;
import com.tinyquest.agent.api.ai.model.request.OpenAIRequestFactory;
import com.tinyquest.agent.api.ai.model.response.OpenAIResponseHandler;
import com.tinyquest.agent.api.ai.rag.RagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final AIClient client;
    private final OpenAIRequestFactory factory;
    private final OpenAIResponseHandler handler;
    private final RagService ragService;
    private final UserMessageLogService messageLogService;
    private final ObjectMapper mapper;

    public String ask(String userMessage
    ) {
        String ragContext = ragService.retrieveContext(userMessage);
        messageLogService.log("jw", userMessage);

        Map<String, Object> request = factory.createChatRequest(userMessage, ragContext);
        try {
            String raw = client.send(mapper.writeValueAsString(request));
            return handler.processResponse(raw, (List<Map<String, Object>>) request.get("messages"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (WebClientResponseException wcre){
            System.err.println("❌ GPT 응답 오류: " + wcre.getResponseBodyAsString());
            throw new RuntimeException(wcre);
        }
    }

    public Flux<String> askStream(
            String userMessage
    ) {
        Map<String, Object> request = factory.createChatRequest(userMessage, "");
        return client.stream(request);
    }
}

