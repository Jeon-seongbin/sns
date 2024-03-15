package com.project.sns.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> emiterMap = new HashMap<>();

    public SseEmitter save(Integer userId, SseEmitter sseEmitter) {
        final String key = getKey(userId);
        emiterMap.put(key, sseEmitter);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Integer userId) {
        return Optional.ofNullable(emiterMap.get(userId));
    }

    public String getKey(Integer userId) {
        return "Emitter:Uid" + userId;
    }

    public void delete(Integer userId) {
        emiterMap.remove(getKey(userId));
    }

}
