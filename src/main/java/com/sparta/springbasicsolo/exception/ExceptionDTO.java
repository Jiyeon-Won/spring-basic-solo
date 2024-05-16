package com.sparta.springbasicsolo.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@AllArgsConstructor
public class ExceptionDTO {

    @Schema(description = "상태코드", example = "에러 코드 (NOT_FOUND, UNAUTHORIZED 등)")
    private HttpStatus statusCode;
    @Schema(description = "에러 메세지", example = "에러 메세지")
    private String message;
}