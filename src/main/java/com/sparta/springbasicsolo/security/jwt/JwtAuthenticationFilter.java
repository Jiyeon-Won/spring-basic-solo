package com.sparta.springbasicsolo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springbasicsolo.domain.user.dto.LoginRequestDto;
import com.sparta.springbasicsolo.domain.user.repository.entity.UserRoleEnum;
import com.sparta.springbasicsolo.security.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        LoginRequestDto requestDto;
        try {
            requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (BadCredentialsException e) {
            throw new JwtException("회원을 찾을 수 없습니다.");
        }
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        null
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createJwtToken(username, role);
        String refreshToken = jwtUtil.createRefreshToken(username, role);
        jwtUtil.addJwtToCookie("access", accessToken, response);
        jwtUtil.addJwtToCookie("refresh", refreshToken, response);

        response.setHeader("Authorization", accessToken);
//        CommonResponseDTO
        response.setContentType("application/json");
        Map<String, String> map = new HashMap<>();
        map.put("message", "로그인 성공");
        map.put("statusCode", HttpStatus.OK.toString());
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(map));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401

        response.setContentType("application/json");
        Map<String, String> map = new HashMap<>();
        map.put("message", "로그인 실패");
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(map));
    }
}