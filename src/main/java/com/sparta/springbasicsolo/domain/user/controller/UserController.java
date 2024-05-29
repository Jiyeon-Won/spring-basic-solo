package com.sparta.springbasicsolo.domain.user.controller;

import com.sparta.springbasicsolo.domain.CommonResponseDTO;
import com.sparta.springbasicsolo.domain.user.dto.SignupRequestDto;
import com.sparta.springbasicsolo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponseDTO<Object>> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok()
                .body(CommonResponseDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("회원가입 성공")
                        .build());
    }
}