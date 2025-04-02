package com.tinyquest.agent.api.ai.function;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FunctionRegistry {
    private final List<FunctionHandler> handlers;

    public FunctionRegistry(List<FunctionHandler> handlers) {
        this.handlers = handlers;
    }

    public List<Map<String, Object>> getFunctionSchemas() {
        return handlers.stream()
                .map(handler -> (Map<String, Object>) handler.getFunctionSchema())
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getToolSchemas() {
        return handlers.stream()
                .map(handler -> Map.of(
                        "type", "function",
                        "function", handler.getFunctionSchema()
                ))
                .collect(Collectors.toList());
    }

    public String executeFunction(String name, String argumentsJson) {
        return handlers.stream()
                .filter(handler -> handler.getName().equals(name))
                .findFirst()
                .map(handler -> handler.invoke(argumentsJson))
                .orElse("함수를 찾을 수 없습니다: " + name);
    }
}
