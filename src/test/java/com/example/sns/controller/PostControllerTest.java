package com.example.sns.controller;

import com.example.sns.controller.request.PostCommentRequest;
import com.example.sns.controller.request.PostCreateRequest;
import com.example.sns.controller.request.PostModifyRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;


    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void createPost() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(post("api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void createPostWhenNotLogin() throws Exception {

        String title = "title";
        String body = "body";

        mockMvc.perform(post("api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void modifyPost() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(put("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void modifyPostNotLogin() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(put("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void modifyPostNotMine() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnSApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(put("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void modifyPostNotPostId() throws Exception {
        String title = "title";
        String body = "body";


        doThrow(new SnSApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(put("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deletePost() throws Exception {
        mockMvc.perform(delete("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void deletePost_NotLogin() throws Exception {
        mockMvc.perform(delete("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void deletePost_AnotherUser() throws Exception {
        doThrow(new SnSApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());
        mockMvc.perform(delete("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deletePost_NoPost() throws Exception {
        doThrow(new SnSApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());
        mockMvc.perform(delete("api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void feedList() throws Exception {
        when(postService.list(any())).thenReturn(Page.empty());
        mockMvc.perform(get("api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void feedListNotLogin() throws Exception {
        when(postService.list(any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void myFeedList() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void myFeedList_NotLogin() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void like() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void like_NotLogin() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void like_NoPost() throws Exception {
        doThrow(new SnSApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).like(any(), any());
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void commnt() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void comment_NotLogin() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(post("api/v1/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void comment_NoPost() throws Exception {
        doThrow(new SnSApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).comment(any(), any());
        mockMvc.perform(post("api/v1/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment"))))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
