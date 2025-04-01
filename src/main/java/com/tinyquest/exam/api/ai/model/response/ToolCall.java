package com.tinyquest.exam.api.ai.model.response;

public record ToolCall(
        String id,
        String type,
        Function function
) {
    public record Function(
            String name,
            String arguments
    ) {}
}
