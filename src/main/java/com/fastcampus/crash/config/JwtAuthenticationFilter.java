package com.fastcampus.crash.config;

import com.fastcampus.crash.service.JwtService;
import com.fastcampus.crash.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter // 시큐리티필터체인 한번만 실행
{

    @Autowired private JwtService jwtService;

    @Autowired private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // TODO : JWT Authentication Logic
        String BEARER_PREFIX = "Bearer ";

        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();
        logger.info("JWT AUTH Start --> " + authorization + "\n");
        logger.info("JWT AUTH Start --> " + securityContext + "\n");

        if(
                !ObjectUtils.isEmpty(authorization) && authorization.startsWith(BEARER_PREFIX)
                && securityContext.getAuthentication() == null)  { // 인증정보가 없거나 비어있지 않으면 인증 X
            var accessToken = authorization.substring(BEARER_PREFIX.length());
            logger.info("JWT Token generate --> " + accessToken + "\n");
            var username = jwtService.getUserName(accessToken); // 토큰 및 유효기간 검증
            var userDetails = userService.loadUserByUsername(username);

            var authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }
        filterChain.doFilter(request, response);

    }
}
