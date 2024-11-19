package com.tripcok.tripcokserver.domain.application.service;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.application.repository.ApplicationRepository;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final GroupRepository groupRepository;
    private final ApplicationRepository applicationRepository;

    /* 모임 신청 */
    @Transactional
    public ResponseEntity<?> createApplication(ApplicationRequestDto.applicationsave request) {
        Application application = new Application();
        Application newApplication = ApplicationRepository.save();
    }

    /* 모임 신청 취소 */
    public ResponseEntity<?> deleteApplication(Long applicationId) {
        try {
            applicationRepository.deleteById(applicationId);
            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 신청을 취소하였습니다.");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


}
