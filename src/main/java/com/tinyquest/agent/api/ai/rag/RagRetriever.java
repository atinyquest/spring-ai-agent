package com.tinyquest.agent.api.ai.rag;

import org.springframework.stereotype.Component;

@Component
public class RagRetriever {
    public String findRelevantContext(String question) {
        return "RAG context for: " + question;
    }
}
