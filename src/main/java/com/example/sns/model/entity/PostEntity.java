package com.example.sns.model.entity;

import com.example.sns.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "\"post\"")
@SQLDelete( sql = "UPDATED \"post\" SET deleted_at = NOW() where id = ?")
@Where(clause =  "deleted_at is NULL")
public class PostEntity {
    @Id
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

}
