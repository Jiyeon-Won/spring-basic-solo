package com.sparta.springbasicsolo.domain.file.service;

import com.sparta.springbasicsolo.domain.file.filedto.FileResponseDTO;
import com.sparta.springbasicsolo.exception.FileException;
import com.sparta.springbasicsolo.domain.file.repository.JpaFileRepository;
import com.sparta.springbasicsolo.domain.file.repository.entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final JpaFileRepository fileRepository;

    public Image saveImage(MultipartFile file) {
        String createdFileName = createFileName(file.getOriginalFilename());

        String absoluteFolderPath = getAbsoluteUploadFolder();

        try {
            file.transferTo(new File(absoluteFolderPath + createdFileName));
            log.info("파일 저장 성공");
        } catch (IOException e) {
            log.error("파일 저장 실패 경로={}", absoluteFolderPath + createdFileName);
            throw new FileException("파일을 저장할 수 없습니다.", e);
        }
        Image image = new Image();
        image.setName(createdFileName);
        image.setPath(absoluteFolderPath);
        return fileRepository.save(image);
    }

    private String getAbsoluteUploadFolder() {
        File file = new File("");
        String currentAbsolutePath = String.valueOf(file.getAbsoluteFile()) + "/upload/";
        log.info("경로={}", currentAbsolutePath);
        Path path = Paths.get(currentAbsolutePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                log.info("업로드 폴더 생성={}", currentAbsolutePath);
            } catch (IOException e) {
                log.error("폴더 경로={}", currentAbsolutePath);
                throw new RuntimeException("사진을 업로드할 폴더를 생성할 수 없습니다.", e);
            }
        }
        return currentAbsolutePath;
    }

    private String createFileName(String originalFileName) {
        int point = originalFileName.lastIndexOf(".");
        String extension = originalFileName.substring(point + 1);
        return UUID.randomUUID() + "." + extension;
    }

    public FileResponseDTO downloadImage(Long id) {
        Image byId = fileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("요청하신 리소스를 찾을 수 없습니다."));
        String url = byId.getPath() + byId.getName();

        return new FileResponseDTO(byId.getName(), new PathResource(url));
    }
}