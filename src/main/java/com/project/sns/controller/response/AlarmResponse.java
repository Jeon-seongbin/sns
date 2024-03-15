package com.project.sns.controller.response;

import com.project.sns.model.Alarm;
import com.project.sns.model.AlarmArgs;
import com.project.sns.model.AlarmType;
import com.project.sns.model.User;
import com.project.sns.model.entity.AlarmEntity;
import com.project.sns.model.entity.UserEntity;
import com.project.sns.repository.AlarmEntityRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class AlarmResponse {

    private Integer id;
    //알람을 받은 사람
//    private User user;
    private AlarmArgs args;
    private AlarmType alarmType;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;


    static public AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
//                alarm.getUser(),
                alarm.getArgs(),
                alarm.getAlarmType(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getRegisteredAt()
        );
    }
}
