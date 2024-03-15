package com.project.sns.configuration;

import com.project.sns.configuration.filter.JwtTokenFilter;
import com.project.sns.exception.CustomAutenticationEntryPoint;
import com.project.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class AutenticationConfig {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String key;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                new RegexRequestMatcher("^(?!/api).*", "POST"),
                new RegexRequestMatcher("^(?!/api).*", "GET"),
                new RegexRequestMatcher("^/api/*/users/join", "GET"),
                new RegexRequestMatcher("^/api/*/users/join", "POST"),
                new RegexRequestMatcher("^/api/*/users/login", "GET"),
                new RegexRequestMatcher("^/api/*/users/login", "POST"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                ).addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
        ;

        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.csrf((csrf) -> csrf.disable());


        http.exceptionHandling().authenticationEntryPoint(new CustomAutenticationEntryPoint());

        return http.build();
    }

}
