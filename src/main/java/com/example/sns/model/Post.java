package com.example.sns.model;

import com.example.sns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {
    private Integer id;

    private String title;

    private String body;

    private User user;

    private Timestamp registerAt;

    private Timestamp updatedAt;

    private Timestamp deleted_at;

    public static Post fromEntity(PostEntity postEntity) {
        return new Post(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getBody(),
                User.fromEntity(postEntity.getUser()),
                postEntity.getRegisterAt(),
                postEntity.getUpdatedAt(),
                postEntity.getDeleted_at()
        );
    }
}