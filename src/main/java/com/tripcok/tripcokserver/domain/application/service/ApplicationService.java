package com.tripcok.tripcokserver.domain.application.service;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.repository.ApplicationRepository;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
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
    public ResponseEntity<?> createApplication(ApplicationRequestDto.save request) {
        Optional<Group> findGroup = groupRepository.findById(request.getGroup_id());

        if (findGroup.isPresent()) {
            Group group = findGroup.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(groupRepository.save(group));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("존재하지 않는 모임입니다.");

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
