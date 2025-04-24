package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import com.example.sns.model.Comment;
import com.example.sns.model.Post;
import com.example.sns.model.entity.*;
import com.example.sns.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity user = getUserEntityOrException(userName);
        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, user));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        PostEntity p = getPostOrException(postId);
        UserEntity user = getUserEntityOrException(userName);
        if (p.getUser() != user) {
            throw new SnSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s different", user.getId()));
        }

        p.setTitle(title);
        p.setBody(body);

        return Post.fromEntity(postEntityRepository.save(p));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        PostEntity p = getPostOrException(postId);
        UserEntity user = getUserEntityOrException(userName);
        if (p.getUser() != user) {
            throw new SnSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s different", user.getId()));
        }
        postEntityRepository.delete(p);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity user = getUserEntityOrException(userName);
        return postEntityRepository.findAllByUser(user, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        PostEntity p = getPostOrException(postId);
        UserEntity user = getUserEntityOrException(userName);
        if (p.getUser() != user) {
            throw new SnSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s different", user.getId()));
        }

        likeEntityRepository.findByUserAndPost(user, p).ifPresent(it -> {
            throw new SnSApplicationException(ErrorCode.ALREADY_LIKED, String.format("username %s already liked post %d", userName, postId));
        });

        likeEntityRepository.save(LikeEntity.of(user, p));
        alarmEntityRepository.save(AlarmEntity.of(p.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(user.getId(), p.getId())));

    }

    public int likeCount(Integer postId) {
        PostEntity p = postEntityRepository.findById(postId).orElseThrow(() -> new SnSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("post %s was not found", postId)));
        return likeEntityRepository.countByPost(p);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        PostEntity p = getPostOrException(postId);
        UserEntity user = getUserEntityOrException(userName);
        if (p.getUser() != user) {
            throw new SnSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s different", user.getId()));
        }

        commentEntityRepository.save(CommentEntity.of(user, p, comment));
        alarmEntityRepository.save(AlarmEntity.of(p.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(user.getId(), p.getId())));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity p = getPostOrException(postId);
        return commentEntityRepository.findAllByPost(p, pageable).map(Comment::fromEntity);
    }

    private PostEntity getPostOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() -> new SnSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("post %s was not found", postId)));
    }

    private UserEntity getUserEntityOrException(String username) {
        return userEntityRepository.findByUserName(username).orElseThrow(() -> new SnSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
    }
}