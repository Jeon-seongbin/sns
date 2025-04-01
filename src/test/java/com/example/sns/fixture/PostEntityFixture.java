package com.example.sns.fixture;

import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String userName, Integer postId, Integer userId) {
        UserEntity u = new UserEntity();
        u.setId(userId);
        u.setUserName(userName);

        PostEntity p = new PostEntity();
        p.setUser(u);
        p.setBody(p.getBody());
        p.setId(postId);

        return p;
    }
}
