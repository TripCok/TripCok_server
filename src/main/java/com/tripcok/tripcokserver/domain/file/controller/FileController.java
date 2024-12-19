package com.tripcok.tripcokserver.domain.file.controller;

import com.tripcok.tripcokserver.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Tag(name = "파일 Swagger")
public class FileController {

    private final FileService fileService;

    /* 이미지 조회 */
    @GetMapping
    @Operation(summary = "이미지 파일 반환", description = "이미지 파일을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "이미지가 성공적으로 반환되었습니다.")
    public ResponseEntity<Resource> getImage(@RequestParam String filePath) {

        Resource imageResource = fileService.loadImageAsResource(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageResource);
    }

}
