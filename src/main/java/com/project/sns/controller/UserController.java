package com.project.sns.controller;

import com.project.sns.controller.request.UserJoinRequest;
import com.project.sns.controller.request.UserLoginRequest;
import com.project.sns.controller.response.AlarmResponse;
import com.project.sns.controller.response.Response;
import com.project.sns.controller.response.UserJoinResponse;
import com.project.sns.controller.response.UserLoginResponse;
import com.project.sns.exception.ErrorCode;
import com.project.sns.exception.SNSApplicationException;
import com.project.sns.model.User;
import com.project.sns.service.UserService;
import com.project.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userservice;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        User user = userservice.join(userJoinRequest.getName(), userJoinRequest.getPassword());
        UserJoinResponse response = UserJoinResponse.fromUser(user);
        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userservice.login(userLoginRequest.getName(), userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication autentication) {
        User user = ClassUtils.getSafeCastInstance(autentication.getPrincipal(), User.class)
                .orElseThrow(() ->
                        new SNSApplicationException(
                                ErrorCode.INTERNAL_SERVER_ERROR
                                , "casting to user class faild"));
        Page<AlarmResponse> alarms = userservice.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm);
        return Response.success(alarms);
    }
}
