package com.tinyquest.exam.api.ai.controller;

import com.tinyquest.exam.api.ai.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class OpenAIRestController {
    private final OpenAIService openAIService;

    @GetMapping("/ask")
    public String ask(
            @RequestParam String message
    ) {

        return openAIService.ask(message);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askStream(
            @RequestParam String message
    ) {
        return openAIService.askStream(message);
    }
}
