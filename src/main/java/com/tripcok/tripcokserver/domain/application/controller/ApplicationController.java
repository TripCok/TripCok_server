package com.tripcok.tripcokserver.domain.application.controller;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/application")
@Getter
@Tag(name = "Application API", description = "모임 지원과 관련된 API")
public class ApplicationController {

    private ApplicationService applicationService;

    /* 모임 신청 */
    @Operation(summary = "모임 신청", description = "모임에 신청합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 성공")
    @PostMapping("/api/v1/{applicationId}")
    public ResponseEntity<?> createApplication(@RequestBody ApplicationRequestDto.applicationsave request) {
        return applicationService.createApplication(request);
    }

    /* 모임 신청 취소 */
    @Operation(summary = "모임 신청 취소", description = "모임 신청을 취소합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 취소 성공")
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long applicationId,
            @RequestParam("memberId") Long memberId) throws AccessDeniedException {
        return applicationService.deleteApplication(applicationId, memberId);
    }


}
