package com.sparta.springbasicsolo.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
public class FileResponseDTO {
    private String fileName;
    private Resource resource;

    public FileResponseDTO(String fileName) {
        this.fileName = fileName;
    }
}