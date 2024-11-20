package com.tripcok.tripcokserver.domain.application.service;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.application.entity.ApplicationMember;
import com.tripcok.tripcokserver.domain.application.repository.ApplicationRepository;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final GroupRepository groupRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    /* 모임 신청 */
    @Transactional
    public ResponseEntity<?> createApplication(ApplicationRequestDto.applicationsave request) {

        Member member = memberRepository.findById(request.getMember_id()).orElseThrow(
                () -> new NoSuchElementException("옳바르지 않은 요청입니다.")
        );

        Group group = groupRepository.findById(request.getGroup_id()).orElseThrow(
                () -> new NotFoundException("옳바르지 않은 요청입니다.")
        );


        /* 이미 신청한 그룹인지 검사 */
        List<ApplicationMember> applicationMembers = group.getApplication().getApplicationMembers();

        for (ApplicationMember applicationMember : applicationMembers) {
            if (applicationMember.getMember().getId().equals(member.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 신청한 모임 입니다.");
            }
        }

        /* 이미 가입되어 있는지 검사 */
        List<GroupMember> groupMembers = group.getGroupMembers();

        for (GroupMember groupMember : groupMembers) {
            if (groupMember.getMember().getId().equals(member.getId())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입되어 있는 모임입니다.");
            }
        }

        Application application = new Application();
        Application applicationSaved = applicationRepository.save(application);

        return ResponseEntity.status(HttpStatus.CREATED).body(applicationSaved);



    }

    private void checkRole(Long memberId) {

    }

    /* 모임 신청 취소 */
    public ResponseEntity<?> deleteApplication(Long applicationId, Long memberId) throws AccessDeniedException {
        // 권한 검사
        checkRole(memberId);

        // 신청서 조회
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("신청을 찾을 수 없습니다. ID: " + applicationId));

        // 신청서 삭제
        applicationRepository.deleteById(applicationId);

        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 신청을 취소했습니다.");

    }


}
