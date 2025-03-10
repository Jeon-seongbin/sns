package com.example.sns.service;

import com.example.sns.exception.SnSApplicationException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @Test
    void adduser(){
        String username = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());
        UserEntity fixture = UserEntityFixture.get(username, password);

        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    void adduser_existUser(){
        String username = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password);

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));

        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));
        SnSApplicationException e = Assertions.assertThrows(SnSApplicationException.class, () -> userService.join(username, password));
    }


    @Test
    void login_user(){
        String username = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password);

        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    void login_noUser(){
        String username = "userName";
        String password = "password";
        when(userEntityRepository.findByUserName(username)).thenReturn((Optional.empty()));

        SnSApplicationException e = Assertions.assertThrows(SnSApplicationException.class, () -> userService.login(username, password));
    }

    @Test
    void login_wrong_password(){
        String username = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";
        UserEntity fixture = UserEntityFixture.get(username, password);

        when(userEntityRepository.findByUserName(username)).thenReturn((Optional.of(fixture)));

        SnSApplicationException e = Assertions.assertThrows(SnSApplicationException.class, () -> userService.login(username, wrongPassword));
    }
}
