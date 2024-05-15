package com.sparta.springbasicsolo.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@AllArgsConstructor
public class ExceptionDTO {
    private HttpStatus StatusCode;
    private String message;
}