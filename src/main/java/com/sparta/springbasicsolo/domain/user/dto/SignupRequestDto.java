package com.sparta.springbasicsolo.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,7}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank
    @Size(min = 4, max = 10, message = "username을 최소 4자 이상, 10자 이하로 입력해 주세요")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자(a~z), 숫자(0~9)만 입력해 주세요.")
    private String username;
    @NotBlank
    @Size(min = 8, max = 15, message = "password를 최소 8자 이상, 15자 이하로 입력해 주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "대소문자(a~z, A~Z), 숫자(0~9)만 입력해 주세요.")
    private String password;
}