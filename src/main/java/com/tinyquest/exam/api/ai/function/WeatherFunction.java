package com.tinyquest.exam.api.ai.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WeatherFunction implements FunctionHandler {

    private final ObjectMapper mapper;

    @Override
    public String getName() {
        return "getWeather";
    }

    @Override
    public String invoke(String argumentsJson) {
        try {
            JsonNode node = mapper.readTree(argumentsJson);
            String location = node.path("location").asText();
            // 실제 로직 대신 mock 응답
            return location + "의 날씨는 맑고 22도입니다.";
        } catch (Exception e) {
            return "날씨 정보를 불러오는 중 오류 발생: " + e.getMessage();
        }
    }

    @Override
    public Object getFunctionSchema() {
        return Map.of(
                "name", this.getName(),
                "description", "지역 이름을 받아 해당 지역의 날씨를 반환합니다.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "location", Map.of(
                                        "type", "string",
                                        "description", "날씨를 조회할 도시 이름"
                                )
                        ),
                        "required", List.of("location")
                )
        );
    }
}
