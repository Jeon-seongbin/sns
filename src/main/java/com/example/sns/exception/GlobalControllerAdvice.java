package com.example.sns.exception;

import com.example.sns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(SnSApplicationException.class)
    public ResponseEntity<?> applicationHandler(SnSApplicationException e){
        log.error("Error occurs {}" , e.getMessage());
        return ResponseEntity.status(
                e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }
}
