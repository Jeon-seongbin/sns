package com.project.sns.controller.response;


import com.project.sns.model.Post;
import com.project.sns.model.User;
import com.project.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostResponse {

    private Integer id;

    private String title;

    private String body;

    private User user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;


    public static PostResponse fromPost(Post post) {

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getUser(),
                post.getRegisteredAt(),
                post.getUpdatedAt(),
                post.getRemovedAt()
        );
    }

}
