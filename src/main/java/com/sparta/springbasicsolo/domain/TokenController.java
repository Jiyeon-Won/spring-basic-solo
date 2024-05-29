package com.sparta.springbasicsolo.domain;

import com.sparta.springbasicsolo.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;

//    @PostMapping("/token/{username}")
//    public ResponseEntity<String> generateToken(@PathVariable String username, HttpServletResponse response) {
//        log.info("토큰 생성");
//        String token = jwtUtil.createJwtToken(username, );
//        jwtUtil.addJwtToCookie(token, response);
//        return ResponseEntity.ok(token);
//    }
}