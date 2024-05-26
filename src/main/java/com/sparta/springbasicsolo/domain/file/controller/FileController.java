package com.sparta.springbasicsolo.domain.file.controller;

import com.sparta.springbasicsolo.domain.CommonResponseDTO;
import com.sparta.springbasicsolo.domain.file.dto.FileRequestDTO;
import com.sparta.springbasicsolo.domain.file.dto.FileResponseDTO;
import com.sparta.springbasicsolo.domain.file.repository.entity.Image;
import com.sparta.springbasicsolo.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "파일 컨트롤러", description = "File API")
public class FileController {

    private final FileService fileService;

    @PostMapping("/image")
    @Operation(summary = "이미지 저장", description = "이미지 1장 저장 (확장자: JPG, PNG, JPEG)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수 요청 변수가 없거나 요청 변수 이름이 잘못되었습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<FileResponseDTO>> uploadImage(@Valid @ModelAttribute FileRequestDTO file) {
        log.info("업로드 할 파일 이름={}", file.getMultipartFile().getOriginalFilename());
        Image savedImage = fileService.saveImage(file.getMultipartFile());

        FileResponseDTO fileResponseDTO = new FileResponseDTO(savedImage.getName());
        return ResponseEntity.ok()
                .body(CommonResponseDTO.<FileResponseDTO>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("파일 등록 성공")
                        .data(fileResponseDTO)
                        .build());
    }

    @GetMapping("/image/{imageName}")
    @Operation(summary = "이미지 다운로드", description = "이미지 다운로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수 요청 변수가 없거나 요청 변수 이름이 잘못되었습니다."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class)))
    })
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageName) {
        FileResponseDTO fileResponseDTO = fileService.downloadImage(imageName);
        log.info("다운로드 파일 이름={}", fileResponseDTO.getFileName());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileResponseDTO.getFileName());
        MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

        try {
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileResponseDTO.getResource().contentLength())
                    .contentType(mediaType)
                    .body(fileResponseDTO.getResource());
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }
}