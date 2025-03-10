package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.model.User;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String username, String password){
       userEntityRepository.findByUserName(username).ifPresent( it -> {
            throw new SnSApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated", username));
        });
        UserEntity user = userEntityRepository.save(UserEntity.of(username, password));
        return User.fromEntity(user);
    }

    public String login(String username, String password){
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(() -> new SnSApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        if (!userEntity.getPassword().equals(password)) {

            throw new SnSApplicationException(ErrorCode.DUPLICATED_USER_NAME, "");
        }

        return "";
    }
}
