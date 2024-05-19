package com.sparta.springbasicsolo.controller.filedto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileRequestDTO {
    private MultipartFile multipartFile;
}