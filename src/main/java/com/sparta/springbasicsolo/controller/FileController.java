package com.sparta.springbasicsolo.controller;

import com.sparta.springbasicsolo.controller.filedto.FileRequestDTO;
import com.sparta.springbasicsolo.controller.filedto.FileResponseDTO;
import com.sparta.springbasicsolo.controller.tododto.TodoResponseDTO;
import com.sparta.springbasicsolo.exception.FileException;
import com.sparta.springbasicsolo.repository.entity.Image;
import com.sparta.springbasicsolo.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "파일 컨트롤러", description = "File API")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload/image")
    @Operation(summary = "이미지 저장", description = "이미지 1장 저장 (확장자: JPG, PNG, JPEG)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "이미지 저장 실패"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseDTO.class)))
    })
    public ResponseEntity<CommonResponseDTO<FileResponseDTO>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException("파일이 존재하지 않습니다.");
        }
        String contentType = file.getContentType();
        if (!isSupportedExtensions(contentType)) {
            throw new FileException("지원되지 않는 확장자 입니다.(JPG, PNG, JPEG)");
        }

        log.info("업로드 할 파일 이름={}", file.getOriginalFilename());
        Optional<Image> savedImage = fileService.saveImage(file);
        if (savedImage.isPresent()) {
            FileResponseDTO fileResponseDTO = new FileResponseDTO(savedImage.get());
            return ResponseEntity.ok()
                    .body(CommonResponseDTO.<FileResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("파일 등록 성공")
                            .data(fileResponseDTO)
                            .build());
        }
        throw new RuntimeException("서버 내부 오류");
    }

    private boolean isSupportedExtensions (String contentType) {
        return contentType.equals("image/jpg") || contentType.equals("image/png") || contentType.equals("image/jpeg");
    }
}