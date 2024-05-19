package com.sparta.springbasicsolo.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponseDTO<T> {
    @Schema(description = "상태코드", example = "200, 401, 404 등")
    private Integer statusCode;
    @Schema(description = "에러 메세지", example = "에러 메세지")
    private String message;
    private T data;
}