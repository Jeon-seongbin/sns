package com.example.sns.fixture;

import com.example.sns.model.entity.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String userName, String password, Integer userId){
        UserEntity u = new UserEntity();
        u.setId(userId);
        u.setUserName(userName);
        u.setPassword(password);
        return u;
    }
}
