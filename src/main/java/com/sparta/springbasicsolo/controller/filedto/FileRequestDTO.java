package com.sparta.springbasicsolo.controller.filedto;

import com.sparta.springbasicsolo.exception.FileException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileRequestDTO {
    private MultipartFile multipartFile;

    public FileRequestDTO(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new FileException("파일이 존재하지 않습니다.");
        }
        String contentType = multipartFile.getContentType();
        if (!isSupportedExtensions(contentType)) {
            throw new FileException("지원하지 않는 확장자 입니다.(JPG, PNG, JPEG)");
        }
        this.multipartFile = multipartFile;
    }

    private boolean isSupportedExtensions(String contentType) {
        return contentType.equals("image/jpg") || contentType.equals("image/png") || contentType.equals("image/jpeg");
    }
}