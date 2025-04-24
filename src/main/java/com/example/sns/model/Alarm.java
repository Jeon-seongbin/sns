package com.example.sns.model;

import com.example.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class Alarm {
    private Integer id;
    private User user;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp registerAt;
    private Timestamp updatedAt;
    private Timestamp deleted_at;

    public static Alarm fromEntity(AlarmEntity alarmEntity) {
        return new Alarm(
                alarmEntity.getId(),
                User.fromEntity(alarmEntity.getUser()),
                alarmEntity.getAlarmType(),
                alarmEntity.getArgs(),
                alarmEntity.getRegisterAt(),
                alarmEntity.getUpdatedAt(),
                alarmEntity.getDeleted_at()
        );
    }
}
