package com.tinyquest.agent.api.ai.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyquest.agent.api.ai.client.AIClient;
import com.tinyquest.agent.api.ai.function.FunctionRegistry;
import com.tinyquest.agent.api.ai.property.OpenAIProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpenAI 응답 처리기 테스트")
class OpenAIResponseHandlerTest {

    @InjectMocks
    private OpenAIResponseHandler handler;

    @Mock
    private FunctionRegistry registry;

    @Mock
    private AIClient client;

    @Mock
    private OpenAIProperties properties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // 강제 주입 (ObjectMapper는 수동으로 넣어야 함)
        handler = new OpenAIResponseHandler(registry, client, objectMapper, properties);
    }

    //should + 동작 + when + 조건
    @Test
    @DisplayName("GPT 거절 실패 테스트")
    void should_returnMessage_when_refusalIsExist() {
        // given
        String rawResponse = "{ \"choices\": [ { \"message\": { \"refusal\": \"내용을 처리할 수 없습니다.\" } } ] }";

        // when
        String result = handler.processResponse(rawResponse, List.of());

        // then
        assertTrue(result.contains("⛔ GPT 응답 거절"));
    }

    @Test
    @DisplayName("일반 요청 테스트 (No 함수 호출)")
    void should_returnContent_when_NoToolOrFunctionCall() {
        // given
        String rawResponse = "{ \"choices\": [ { \"message\": { \"content\": \"서울은 맑아요.\" } } ] }";

        // when
        String result = handler.processResponse(rawResponse, List.of());

        // then
        assertEquals("서울은 맑아요.", result);
    }

    @Test
    @DisplayName("함수호출 성공 및 팔로우 메세지 발송 테스트")
    void should_HandleFunctionCallCorrectly_and_SendFollowup() {
        // given
        String rawResponse = """
        {
          "choices": [
            {
              "message": {
                "function_call": {
                  "name": "getWeather",
                  "arguments": {
                    "location": "서울"
                  }
                }
              }
            }
          ]
        }
        """;

        when(registry.executeFunction(eq("getWeather"), anyString())).thenReturn("맑고 화창해요");
        when(properties.getModel()).thenReturn("gpt-4");
        when(client.send(anyString())).thenReturn("{ \"choices\": [ { \"message\": { \"content\": \"서울은 맑고 화창합니다.\" } } ] }");

        // when
        String result = handler.processResponse(rawResponse, List.of());

        // then
        assertEquals("서울은 맑고 화창합니다.", result);
    }

    @Test
    @DisplayName("JSON parser 실패 테스트")
    void should_HandleJsonParseError_gracefully() {
        // given
        String invalidJson = "{ this is not valid JSON }";

        // when
        String result = handler.processResponse(invalidJson, List.of());

        // then
        assertTrue(result.startsWith("❌ 응답 처리 오류:"));
    }

    @Test
    @DisplayName("최종 응답 파싱 에러 테스트")
    void should_handleFinalAnswerParseError_gracefully() {
        // given
        String rawResponse = """
        {
          "choices": [
            {
              "message": {
                "function_call": {
                  "name": "getWeather",
                  "arguments": "{}"
                }
              }
            }
          ]
        }
        """;

        when(registry.executeFunction(eq("getWeather"), anyString())).thenReturn("맑음");
        when(properties.getModel()).thenReturn("gpt-4");
        when(client.send(anyString())).thenReturn("{ not valid completion response }");

        // when
        String result = handler.processResponse(rawResponse, List.of());

        // then
        assertTrue(result.startsWith("❌ 최종 응답 파싱 실패:"));
    }
}