package com.example.sns.configuration;

import com.example.sns.configuration.filter.JwtTokenFilter;
import com.example.sns.exception.CustomAuthenticationEntryPoint;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig  {

    private final UserService u;

    @Value("${jwt.secret-key}")
    private String secretKey;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .headers((headerConfig) ->
                        headerConfig.frameOptions(frameOptionsConfig ->
                                frameOptionsConfig.disable()
                        )
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/*/users/join").permitAll()
                                .requestMatchers("/api/*/users/login").permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음
                ).
                addFilterBefore(new JwtTokenFilter(secretKey, u), UsernamePasswordAuthenticationFilter.class);
            //    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());


        return http.build();
    }
}
