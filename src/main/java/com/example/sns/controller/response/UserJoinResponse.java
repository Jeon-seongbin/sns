package com.example.sns.controller.response;

import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Integer id;
    private String name;

    public static UserJoinResponse fromUser(UserEntity user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUserName()
        );
    }
}
