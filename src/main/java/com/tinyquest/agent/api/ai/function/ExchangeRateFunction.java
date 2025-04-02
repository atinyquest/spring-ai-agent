package com.tinyquest.agent.api.ai.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExchangeRateFunction implements FunctionHandler {

    private final ObjectMapper mapper;

    @Override
    public String getName() {
        return "getExchangeRateFunction";
    }

    @Override
    public String invoke(String argumentsJson) {
        try {
            JsonNode node = mapper.readTree(argumentsJson);
            String currency = node.path("currency").asText();
            return currency + "의 환율은 1,300원입니다.";
        } catch (Exception e) {
            return "환율 정보를 불러오는 중 오류 발생: " + e.getMessage();
        }
    }

    @Override
    public Object getFunctionSchema() {
         return Map.of(
                "name", this.getName(),
                "description", "통화 코드를 받아 해당 환율을 반환합니다.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "currency", Map.of(
                                        "type", "string",
                                        "description", "환율을 조회할 통화 코드 (예: USD, EUR)"
                                )
                        ),
                        "required", List.of("currency")
                )
        );
    }
}
