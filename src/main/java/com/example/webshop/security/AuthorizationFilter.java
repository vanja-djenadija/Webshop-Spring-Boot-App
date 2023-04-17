package com.example.webshop.security;

import com.example.webshop.models.dto.JWTUser;
import com.example.webshop.models.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    @Value("${authorization.token.header.name}")
    private String authorizationHeaderName;
    @Value("${authorization.token.header.prefix}")
    private String authorizationHeaderPrefix;
    @Value("${authorization.token.secret}")
    private String authorizationSecret;
    private final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(authorizationHeaderName);
        if (authorizationHeader == null || !authorizationHeader.startsWith(authorizationHeaderPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(authorizationHeaderPrefix, "");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(authorizationSecret)
                    .parseClaimsJws(token)
                    .getBody();
            JWTUser jwtUser = new JWTUser(Integer.valueOf(claims.getId()), claims.getSubject(), null, UserStatus.valueOf(claims.get("role", String.class)));
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error(String.format("JWT Authentication failed from: %s", request.getRemoteHost()));
        }
        filterChain.doFilter(request, response);
    }
}