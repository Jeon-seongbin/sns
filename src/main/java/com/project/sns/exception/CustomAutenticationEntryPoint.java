package com.project.sns.exception;

import com.project.sns.controller.response.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAutenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("applciation/json");
        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());

        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());

    }
}
