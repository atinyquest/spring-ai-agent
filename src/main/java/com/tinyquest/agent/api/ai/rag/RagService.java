package com.tinyquest.agent.api.ai.rag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {
    private final RagRetriever retriever;

    public String retrieveContext(String question) {
        return retriever.findRelevantContext(question);
    }

}
