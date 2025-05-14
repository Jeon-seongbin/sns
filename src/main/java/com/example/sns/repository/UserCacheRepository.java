package com.example.sns.repository;

import com.example.sns.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    private final RedisTemplate<String, User> userRedisTemplate;
    public final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        userRedisTemplate.opsForValue().set(getKey(user.getUsername()), user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String userName) {
        return Optional.ofNullable(userRedisTemplate.opsForValue().get(userName));
    }

    private String getKey(String userName) {
        return "USER" + userName;
    }
}