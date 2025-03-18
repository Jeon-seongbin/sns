package com.example.sns.model;

import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class User implements UserDetails {

    private Integer id;

    private String userName;

    private String password;

    private UserRole role;

    private Timestamp registerAt;

    private Timestamp updatedAt;

    private Timestamp deleted_at;

    public static User fromEntity(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getRegisterAt(),
                userEntity.getUpdatedAt(),
                userEntity.getDeleted_at()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

}
