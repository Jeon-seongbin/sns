package com.project.sns.service;

import com.project.sns.configuration.AutenticationConfig;
import com.project.sns.exception.ErrorCode;
import com.project.sns.exception.SNSApplicationException;
import com.project.sns.model.Alarm;
import com.project.sns.model.User;
import com.project.sns.model.entity.UserEntity;
import com.project.sns.repository.AlarmEntityRepository;
import com.project.sns.repository.UserCacheRepository;
import com.project.sns.repository.UserEntityRepository;
import com.project.sns.util.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserCacheRepository userCacheRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTomeMs;

    public User loadUserByUsername(String userName) {
        return userCacheRepository.getUser(userName).orElseGet(() ->
                userRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                        new SNSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))
                ));

    }

    @Transactional
    public User join(String username, String password) {

        userRepository.findByUserName(username).ifPresent(it -> {
            throw new SNSApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username));
        });

        UserEntity userEntity = userRepository.save(UserEntity.of(username, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    public String login(String username, String password) {
        User user = loadUserByUsername(username);
        if (!encoder.matches(password, user.getPassword())) {
            throw new SNSApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        userCacheRepository.setUser(user);
        return JwtTokenUtils.generateToken(username, secretKey, expiredTomeMs);
    }

    public Page<Alarm> alarmList(Integer userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);
    }
}