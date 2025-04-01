package com.example.sns.controller.response;

import com.example.sns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public
class PostResponse {
    private Integer id;
    private String title;
    private String body;
    private UserResponse user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                UserResponse.fromUser(post.getUser()),
                post.getRegisterAt(),
                post.getUpdatedAt()
        );
    }
}