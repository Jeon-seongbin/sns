package com.example.sns.service;

import com.example.sns.controller.model.User;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String username, String password){
        Optional<UserEntity> userEntity = userEntityRepository.findByUsername(username);
        userEntityRepository.save(new UserEntity());
        return new User();
    }

    public String login(String username, String password){
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new SnSApplicationException());

        if (!userEntity.getPassword().equals(password)) {

            throw new SnSApplicationException();
        }

        return "";
    }
}
