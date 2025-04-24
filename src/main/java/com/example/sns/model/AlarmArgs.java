package com.example.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlarmArgs {
    // alarm occurred user
    private Integer fromUserId;

    private Integer targetId;

}
