package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.model.User;
import com.example.sns.exception.SnSApplicationException;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.util.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;




    @Transactional
    public User join(String username, String password){
       userEntityRepository.findByUserName(username).ifPresent( it -> {
            throw new SnSApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated", username));
        });
        UserEntity user = userEntityRepository.save(UserEntity.of(username, encoder.encode(password)));
        return User.fromEntity(user);
    }

    public String login(String username, String password){
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(() -> new SnSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded username", username)));

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnSApplicationException(ErrorCode.INVALID_PASSWORD);
        }

       return JwtTokenUtils.generateToken(username,secretKey, expiredTimeMs);

    }
}
