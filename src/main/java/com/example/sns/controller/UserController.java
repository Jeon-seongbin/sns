package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.response.Response;
import com.example.sns.controller.response.UserJoinResponse;
import com.example.sns.model.User;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        User user = userService.join(request.getUserName(),request.getPassword());
        UserJoinResponse response = UserJoinResponse.fromUser(user);
        return Response.success(response);
    }
}