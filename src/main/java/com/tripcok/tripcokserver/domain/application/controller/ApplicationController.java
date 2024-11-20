package com.tripcok.tripcokserver.domain.application.controller;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/application")
@Getter
@Tag(name = "Application API", description = "모임 지원과 관련된 API")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /* 모임 신청 */
    @Operation(summary = "모임 신청", description = "모임에 신청합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 성공")
    @PostMapping
    public ResponseEntity<?> createApplication(@RequestBody ApplicationRequestDto.applicationSave request) {
        return applicationService.createApplication(request);
    }

    /* 모임 신청 취소 */
    @Operation(summary = "모임 신청 취소", description = "모임 신청을 취소합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 취소 성공")
    @DeleteMapping(("/{applicationId}"))
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long applicationId,
            @RequestParam("memberId") Long memberId) throws AccessDeniedException {
        return applicationService.deleteApplication(applicationId, memberId);
    }

    /* 모임 가입 완료 */
    @Operation(summary = "모임 가입 완료", description = "모임 가입이 완료되었습니다.")
    @ApiResponse(responseCode = "200", description = "모임 가입 완료")
    @PutMapping("/accept")
    public ResponseEntity<?> acceptApplication(@RequestBody ApplicationRequestDto.applicationAccept request) {
        return applicationService.acceptApplication(request);
    }

    /* 모임 신청 수락 */
    @Operation(summary = "모임 신청 수락", description = "모임 신청을 수락하였습니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 수락 성공")
    @PutMapping
    public ResponseEntity<?> acceptedApplication(@RequestBody ApplicationRequestDto.applicationAccept request) {
        return applicationService.acceptedApplication(request);
    }
}
