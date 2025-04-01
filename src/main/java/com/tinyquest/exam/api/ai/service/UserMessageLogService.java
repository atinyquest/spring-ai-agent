package com.tinyquest.exam.api.ai.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMessageLogService {
    private final Map<String, List<String>> userLogs = new HashMap<>();

    public void log(String userId, String message) {
        userLogs.computeIfAbsent(userId, id -> new ArrayList<>()).add(message);
    }

    public List<String> getLogs(String userId) {
        return userLogs.getOrDefault(userId, List.of());
    }
}
