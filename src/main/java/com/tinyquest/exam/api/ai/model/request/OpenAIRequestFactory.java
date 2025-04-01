package com.tinyquest.exam.api.ai.model.request;

import com.tinyquest.exam.api.ai.function.FunctionRegistry;
import com.tinyquest.exam.api.ai.property.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAIRequestFactory {

    private final OpenAIProperties properties;
    private final FunctionRegistry functionRegistry;

    public Map<String, Object> createChatRequest(String userMessage, String ragContext) {
        Map<String, Object> request = new LinkedHashMap<>();
        // request.put("tool_choice", "auto");
        request.put("model", properties.getModel());
        request.put("stream", properties.isStream());

        request.put("messages", List.of(
                Map.of("role", "system", "content", "당신은 사용자 요청에 따라 적절한 함수를 호출하는 어시스턴트입니다."),
                Map.of("role", "system", "content", "참고 문서: " + ragContext),
                Map.of("role", "user", "content", userMessage)
        ));

        request.put("functions", functionRegistry.getFunctionSchemas()); // 최신 방식

        return request;
    }
}
