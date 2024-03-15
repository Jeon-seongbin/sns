package com.project.sns.configuration.filter;

import com.project.sns.model.User;
import com.project.sns.service.UserService;
import com.project.sns.util.JwtTokenUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;
    private final RequestMatcher ignoredPathsJoin = new AntPathRequestMatcher("/api/*/users/join");
    private final RequestMatcher ignoredPathsLogin = new AntPathRequestMatcher("/api/*/users/login");

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token;
        try {

            if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                token = request.getQueryString().split("=")[1].trim();
            } else {
                if (this.ignoredPathsJoin.matches(request)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                if (this.ignoredPathsLogin.matches(request)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (header == null || !header.startsWith("Bearer ")) {
                    logger.error("Error occurs while getting header. header is null or invalid");
                    filterChain.doFilter(request, response);
                    return;
                }
                token = header.split(" ")[1].trim();
            }

            if (JwtTokenUtils.isExpired(token, key)) {
                logger.error("key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String username = JwtTokenUtils.userName(token, key);

            User user = userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken autentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            autentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(autentication);
        } catch (RuntimeException e) {
            logger.error("Error occurs while validating. {}");

            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
