package com.sparta.springbasicsolo.service;

import com.sparta.springbasicsolo.controller.filedto.FileResponseDTO;
import com.sparta.springbasicsolo.exception.FileException;
import com.sparta.springbasicsolo.repository.JpaFileRepository;
import com.sparta.springbasicsolo.repository.entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final JpaFileRepository fileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public Image saveImage(MultipartFile file) {
        String createdFileName = createFileName(file.getOriginalFilename());

        try {
            file.transferTo(new File(fileDir + createdFileName));
            log.info("파일 저장 성공");
        } catch (IOException e) {
            log.error("파일 저장 실패");
            throw new FileException("파일을 저장할 수 없습니다.", e);
        }
        Image image = new Image();
        image.setName(createdFileName);
        image.setPath(fileDir);
        return fileRepository.save(image);
    }

    private String createFileName(String originalFileName) {
        int point = originalFileName.lastIndexOf(".");
        String extension = originalFileName.substring(point + 1);
        return UUID.randomUUID() + "." + extension;
    }

    public FileResponseDTO downloadImage(Long id) {
        Image byId = fileRepository.findById(id).orElseThrow(() -> new NoSuchElementException("요청하신 리소스를 찾을 수 없습니다."));
        String url = byId.getPath() + byId.getName();

        return new FileResponseDTO(byId.getName(), new PathResource(url));
    }
}