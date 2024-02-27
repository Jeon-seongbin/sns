package com.project.sns.controller.fixture;

import com.project.sns.model.entity.PostEntity;
import com.project.sns.model.entity.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String username, String password, Integer postid) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUserName(username);

        PostEntity result = new PostEntity();
        result.setUser(userEntity);
        result.setId(postid);

        return result;
    }
}
