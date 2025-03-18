package com.example.sns.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "\"post\"")
@SQLDelete(sql = "UPDATED \"post\" SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class PostEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "register_at")
    private Timestamp registerAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deleted_at;

    public static PostEntity of(String title, String body, UserEntity userEntity) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setBody(body);
        postEntity.setUser(userEntity);
        return postEntity;
    }
}
