package com.example.sns.configuration.filter;


import com.example.sns.model.User;
import com.example.sns.service.UserService;
import com.example.sns.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = "";
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Enumeration<String> headerNames = httpRequest.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String nextElem = headerNames.nextElement();
                log.info("nextElem");
                log.info(nextElem);
                if (nextElem.equals("authorization")) {
                    header = httpRequest.getHeader(nextElem);
                    break;
                }
            }
        }

        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting header.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();
            log.info(token);
            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String userName = JwtTokenUtils.getUserName(token, key);
            User u = userService.loadUserByUserName(userName);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (RuntimeException error) {
            log.error("Error occurs {}", error.toString());
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
