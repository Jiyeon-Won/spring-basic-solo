package com.sparta.springbasicsolo.security.controller;

import com.sparta.springbasicsolo.domain.user.repository.entity.UserRoleEnum;
import com.sparta.springbasicsolo.security.jwt.JwtUtil;
import com.sparta.springbasicsolo.security.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/token/{username}")
    public ResponseEntity<String> generateToken(@PathVariable String username, HttpServletResponse response) {
        log.info("토큰 생성");
        String token = jwtUtil.createJwtToken(username, UserRoleEnum.USER);
        jwtUtil.addJwtToCookie("access", token, response);
        return ResponseEntity.ok(token);
    }
}