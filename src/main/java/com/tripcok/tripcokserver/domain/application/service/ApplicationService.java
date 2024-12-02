package com.tripcok.tripcokserver.domain.application.service;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.dto.ApplicationResponseDto;
import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.application.repository.ApplicationRepository;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final GroupRepository groupRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    /* 모임 신청 생성 */
    @Transactional
    public ResponseEntity<String> createApplication(ApplicationRequestDto.applicationSave request) {
        Group group = getGroupById(request.getGroupId());
        Member member = getMemberById(request.getMemberId());

        // 이미 신청된 경우 처리
        if (applicationRepository.findByGroupAndMember(group, member).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 모임에 신청하셨습니다.");
        }

        // 신청 생성 및 저장
        Application application = applicationRepository.save(new Application(member, group));
        group.addApplication(application);

        return ResponseEntity.status(HttpStatus.CREATED).body("성공적으로 모임에 신청되었습니다.");
    }

    /* 모임 신청 취소 */
    @Transactional
    public ResponseEntity<String> deleteApplication(Long applicationId, Long memberId) throws AccessDeniedException {
        Application application = getApplicationById(applicationId);
        checkRole(memberId, application.getGroup(), GroupRole.ADMIN);

        applicationRepository.delete(application);
        return ResponseEntity.status(HttpStatus.OK).body("신청이 성공적으로 취소되었습니다.");
    }

    /* 모임 신청 수락 */
    @Transactional
    public ResponseEntity<String> acceptApplication(ApplicationRequestDto.applicationAccept request) throws AccessDeniedException {
        Application application = getApplicationById(request.getApplicationId());
        Group group = application.getGroup();

        // 권한 확인
        checkRole(request.getGroupAdminId(), group, GroupRole.ADMIN);

        // 멤버 추가
        GroupMember newMember = new GroupMember(application.getMember(), group, GroupRole.MEMBER);
        groupMemberRepository.save(newMember);

        // 신청 삭제
        applicationRepository.delete(application);

        return ResponseEntity.ok("신청이 수락되었으며, 모임에 가입이 완료되었습니다.");
    }

    /* 특정 모임의 신청 목록 조회 */
    public ResponseEntity<?> getApplications(Long groupId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        Page<Application> applications = applicationRepository.findByGroupIdOrderByCreateAtDesc(groupId, pageable);

        return ResponseEntity.ok(applications.map(ApplicationResponseDto::new));
    }

    /* 권한 확인 */
    private void checkRole(Long memberId, Group group, GroupRole requiredRole) throws AccessDeniedException {
        boolean hasRole = groupMemberRepository.findByGroupInAdminMember(group.getId(), requiredRole, memberId).isPresent();
        if (!hasRole) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    /* ID로 그룹 조회 */
    private Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다. ID: " + groupId));
    }

    /* ID로 회원 조회 */
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. ID: " + memberId));
    }

    /* ID로 신청서 조회 */
    private Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("신청서를 찾을 수 없습니다. ID: " + applicationId));
    }
}
