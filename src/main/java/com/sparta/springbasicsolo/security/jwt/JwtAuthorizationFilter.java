package com.sparta.springbasicsolo.security.jwt;

import com.sparta.springbasicsolo.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/user/login".equals(request.getRequestURI())
                || "/user/signup".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        }

        String token = jwtUtil.getTokenFromRequest(request);
        if (StringUtils.hasText(token)) {
            token = jwtUtil.substringToken(token);
            log.info("토큰 확인 = {}", token);

            if (!jwtUtil.validateToken(token)) {
                log.error("토큰 에러");
                return;
            }
            Claims info = jwtUtil.getBodyFromToken(token);
            String subject = info.getSubject();

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}