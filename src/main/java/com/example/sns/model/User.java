package com.example.sns.model;

import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class User {

    private Integer id;

    private String userName;

    private String password;

    private UserRole role;

    private Timestamp registerAt;

    private Timestamp updatedAt;

    private Timestamp deleted_at;

    public static User fromEntity(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getRegisterAt(),
                userEntity.getUpdatedAt(),
                userEntity.getDeleted_at()
        );
    }
}
