package com.sparta.springbasicsolo.security.jwt;

import com.sparta.springbasicsolo.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
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
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/user")
                || (requestUri.startsWith("/todo") && request.getMethod().equals("GET"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtUtil.getTokenFromRequest("access", request);
        String refreshToken = jwtUtil.getTokenFromRequest("refresh", request);

        if (StringUtils.hasText(accessToken)) {
            if (jwtUtil.validateToken(accessToken)) {
                logic(accessToken);
            } else {
                log.info("access 토큰 만료");
                if (StringUtils.hasText(refreshToken)) {
                    if (jwtUtil.validateToken(refreshToken)) {
                        String newAccessToken = jwtUtil.newAccessToken(refreshToken);
                        jwtUtil.addJwtToCookie("access", newAccessToken, response);
                        String substringToken = jwtUtil.substringToken(newAccessToken);
                        logic(substringToken);
                    } else {
                        log.info("refresh 토큰 만료");
                        throw new JwtException("로그인이 필요합니다.");
                    }
                } else {
                    throw new JwtException("로그인이 필요합니다.");
                }
            }
        } else {
            throw new JwtException("로그인이 필요합니다.");
        }
        filterChain.doFilter(request, response);
    }

    private void logic(String accessToken) {
        String username = jwtUtil.getUsername(accessToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}