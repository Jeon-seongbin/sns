package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.fixture.PostEntityFixture;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockitoBean
    private PostEntityRepository postEntityRepository;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @Test
    void createPost() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }


    @Test
    void createPost_noUser() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnSApplicationException e = Assertions.assertThrows(SnSApplicationException.class, () -> postService.create(title, body, userName));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void createPost_postNotFound() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity p = PostEntityFixture.get(userName, postId, 1);
        UserEntity u = p.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(u));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnSApplicationException e = Assertions.assertThrows(SnSApplicationException.class, () -> postService.modify(title, body, userName, postId));

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

}
