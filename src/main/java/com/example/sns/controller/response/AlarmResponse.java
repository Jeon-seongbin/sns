package com.example.sns.controller.response;

import com.example.sns.model.Alarm;
import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private UserResponse user;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp registerAt;
    private Timestamp updatedAt;
    private Timestamp deleted_at;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                UserResponse.fromUser(alarm.getUser()),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getRegisterAt(),
                alarm.getUpdatedAt(),
                alarm.getDeleted_at()
        );
    }
}
