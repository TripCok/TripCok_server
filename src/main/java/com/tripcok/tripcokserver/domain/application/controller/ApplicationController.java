package com.tripcok.tripcokserver.domain.application.controller;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.service.ApplicationService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/application")
@Getter
public class ApplicationController {

    private ApplicationService applicationService;

    /* 모임 신청 */
    @PostMapping
    public ResponseEntity<?> createApplication(@RequestBody ApplicationRequestDto.save request) {
        return applicationService.createApplication(request);
    }

    /* 모임 신청 취소 */
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long applicationId) {
        return applicationService.deleteApplication(applicationId);
    }


}
