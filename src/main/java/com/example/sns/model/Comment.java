package com.example.sns.model;

import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Comment {
    private Integer id;

    private String comment;

    private String userName;

    private Integer postId;

    private Timestamp registerAt;

    private Timestamp updatedAt;

    private Timestamp deleted_at;

    public static Comment fromEntity(CommentEntity commentEntity) {
        return new Comment(
                commentEntity.getId(),
                commentEntity.getComment(),
                commentEntity.getUser().getUserName(),
                commentEntity.getId(),
                commentEntity.getRegisterAt(),
                commentEntity.getUpdatedAt(),
                commentEntity.getDeleted_at()
        );
    }
}