package com.sparta.springbasicsolo.domain.file.service;

import com.sparta.springbasicsolo.domain.file.dto.FileResponseDTO;
import com.sparta.springbasicsolo.domain.file.repository.JpaFileRepository;
import com.sparta.springbasicsolo.domain.file.repository.entity.Image;
import com.sparta.springbasicsolo.exception.FileException;
import com.sparta.springbasicsolo.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final JpaFileRepository fileRepository;

    public Image saveImage(MultipartFile file) {
        String createdFileName = FileUtils.createFileName(file.getOriginalFilename());

        String absoluteFolderPath = FileUtils.getAbsoluteUploadFolder();

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

    public FileResponseDTO downloadImage(Long id) {
        Image byId = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 리소스를 찾을 수 없습니다."));
        String url = byId.getPath() + byId.getName();

        return new FileResponseDTO(byId.getName(), new PathResource(url));
    }
}