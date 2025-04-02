package com.tinyquest.agent.api.ai.model.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompletionResponse(List<Choice> choices,
                                 String id,
                                 String object,
                                 long created,
                                 String model,
                                 Usage usage
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(
            Message message,
            int index,
            @JsonProperty("finish_reason") String finishReason
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(
            String role,
            String content,
            @JsonProperty("tool_calls") List<ToolCall> toolCalls,
            @JsonProperty("function_call") FunctionCall functionCall,
            String refusal,
            List<Annotation> annotations
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ToolCall(
            String id,
            String type,
            FunctionCall function
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FunctionCall(
            String name,
            JsonNode arguments
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Annotation(
            String type,
            String text,
            String explanation
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Usage(
            @JsonProperty("prompt_tokens") int promptTokens,
            @JsonProperty("completion_tokens") int completionTokens,
            @JsonProperty("total_tokens") int totalTokens
    ) {}
}
