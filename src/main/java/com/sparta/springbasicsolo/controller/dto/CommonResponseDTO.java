package com.sparta.springbasicsolo.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponseDTO<T> {
    private Integer statusCode;
    private T data;
}