package com.project.sns.service;

import com.project.sns.exception.ErrorCode;
import com.project.sns.exception.SNSApplicationException;
import com.project.sns.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String ALARM_NAME = "alarm";

    private final EmitterRepository emitterRepository;

    public void send(Integer alarmId, Integer userId) {
        emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {


            try {
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(userId);
                throw new SNSApplicationException(ErrorCode.ALARM_CONNECTION_ERROR);
            }
        }, () -> {

        });
    }

    public SseEmitter connectionAlarm(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);
        sseEmitter.onCompletion(() -> {
            emitterRepository.delete(userId);
        });
        sseEmitter.onTimeout(() -> {
            emitterRepository.delete(userId);
        });

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME)
                    .data("connect completed"));
        } catch (IOException e) {
            throw new SNSApplicationException(ErrorCode.ALARM_CONNECTION_ERROR);
        }

        return sseEmitter;

    }
}
