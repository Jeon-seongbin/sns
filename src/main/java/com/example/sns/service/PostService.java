package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.model.Post;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
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

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity user = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, user));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        UserEntity user = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        PostEntity p = postEntityRepository.findById(postId).orElseThrow(() -> new SnSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("post %s was not found", postId)));

        if (p.getUser() != user) {
            throw new SnSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s different", user.getId()));
        }

        p.setTitle(title);
        p.setBody(body);

        return Post.fromEntity(postEntityRepository.save(p));
    }

    @Transactional
    public void delete(String userName, Integer postId) {

        UserEntity user = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        PostEntity p = postEntityRepository.findById(postId).orElseThrow(() -> new SnSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("post %s was not found", postId)));

        if (p.getUser() != user) {
            throw new SnSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s different", user.getId()));
        }

        postEntityRepository.delete(p);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity user = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        return postEntityRepository.findAllByUser(user, pageable).map(Post::fromEntity);
    }
}