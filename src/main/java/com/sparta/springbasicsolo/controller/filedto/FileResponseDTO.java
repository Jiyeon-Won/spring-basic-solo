package com.sparta.springbasicsolo.controller.filedto;

import com.sparta.springbasicsolo.repository.entity.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponseDTO {
    private String fileName;

    public FileResponseDTO(Image image) {
        this.fileName = image.getName();
    }
}