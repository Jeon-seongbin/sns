package com.project.sns.model;

import com.project.sns.model.entity.AlarmEntity;
import com.project.sns.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class Alarm {

    private Integer id;
    //알람을 받은 사람
    private UserEntity user;
    private AlarmArgs args;
    private AlarmType alarmType;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;


    static public Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getId(),
                entity.getUser(),
                entity.getArgs(),
                entity.getAlarmType(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRegisteredAt()
        );
    }
}
