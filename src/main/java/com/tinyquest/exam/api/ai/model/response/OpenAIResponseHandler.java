package com.tinyquest.exam.api.ai.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyquest.exam.api.ai.client.AIClient;
import com.tinyquest.exam.api.ai.function.FunctionRegistry;
import com.tinyquest.exam.api.ai.property.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.tinyquest.exam.api.ai.model.response.CompletionResponse.Annotation;
import com.tinyquest.exam.api.ai.model.response.CompletionResponse.FunctionCall;
import com.tinyquest.exam.api.ai.model.response.CompletionResponse.ToolCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAIResponseHandler {

    private final FunctionRegistry registry;
    private final AIClient client;
    private final ObjectMapper objectMapper;
    private final OpenAIProperties properties;

    public String processResponse(String rawResponse, List<Map<String, Object>> originalMessages) {
        try {
            JsonNode messageNode = objectMapper.readTree(rawResponse)
                    .path("choices").get(0)
                    .path("message");

            // 1️⃣ refusal 최우선 처리
            boolean isRefusal = messageNode.has("refusal")
                    && !messageNode.path("refusal").isNull()
                    && !messageNode.path("refusal").asText().isBlank();

            if (isRefusal) {
                return "⛔ GPT 응답 거절: " + messageNode.path("refusal").asText();
            }

            // 2️⃣ tool_calls 또는 function_call 없으면 content 반환 (추가 요청 X)
            boolean hasToolCalls = messageNode.has("tool_calls");
            boolean hasFunctionCall = messageNode.has("function_call");

            if (!hasToolCalls && !hasFunctionCall) {
                // annotations 참고용 로그
                if (messageNode.has("annotations")) {
                    List<Annotation> annotations = objectMapper.convertValue(
                            messageNode.path("annotations"),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, Annotation.class));
                    annotations.forEach(annotation -> {
                        System.out.println("📎 Annotation → type: " + annotation.type()
                                + ", explanation: " + annotation.explanation());
                    });
                }
                return messageNode.path("content").asText();
            }

            // 3️⃣ tool/function 호출이 있을 경우 → 후속 메시지 구성
            List<Map<String, Object>> followupMessages = new ArrayList<>(originalMessages);
            followupMessages.add(objectMapper.convertValue(messageNode, Map.class));

            if (hasToolCalls) {
                for (JsonNode callNode : messageNode.path("tool_calls")) {
                    ToolCall call = objectMapper.treeToValue(callNode, ToolCall.class);
                    String result = registry.executeFunction(call.function().name(), call.function().arguments());
                    followupMessages.add(Map.of(
                            "role", "tool",
                            "tool_call_id", call.id(),
                            "name", call.function().name(),
                            "content", result
                    ));
                }
            }

            if (hasFunctionCall) {
                FunctionCall func = objectMapper.treeToValue(
                        messageNode.path("function_call"), FunctionCall.class);
                String result = registry.executeFunction(func.name(), func.arguments());
                followupMessages.add(Map.of(
                        "role", "function",
                        "name", func.name(),
                        "content", result
                ));
            }

            // 4️⃣ 후속 요청 전송
            Map<String, Object> followupRequest = Map.of(
                    "model", properties.getModel(),
                    "messages", followupMessages
            );

            String finalResponse = client.send(objectMapper.writeValueAsString(followupRequest));
            return extractFinalAnswer(finalResponse);

        } catch (Exception e) {
            return "❌ 응답 처리 오류: " + e.getMessage();
        }
    }

    private String extractFinalAnswer(String rawJson) {
        try {
            CompletionResponse response = objectMapper.readValue(rawJson, CompletionResponse.class);
            return response.choices().getFirst().message().content();
        } catch (Exception e) {
            return "❌ 최종 응답 파싱 실패: " + e.getMessage();
        }
    }
}
