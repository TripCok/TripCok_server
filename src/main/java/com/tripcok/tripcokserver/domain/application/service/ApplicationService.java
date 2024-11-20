package com.tripcok.tripcokserver.domain.application.service;

import com.tripcok.tripcokserver.domain.application.dto.ApplicationRequestDto;
import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.application.repository.ApplicationRepository;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final GroupRepository groupRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    /* 모임 신청 */
    @Transactional
    public ResponseEntity<?> createApplication(ApplicationRequestDto.applicationSave request) {

        /* 신청을 넣으려는 그룹을 찾기 */
        Optional<Group> findGroup = groupRepository.findById(request.getGroupId());

        if (findGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("모임을 찾을 수 없습니다.");
        }

        Group group = findGroup.get();

        /* 새로운 Application 을 생성 */
        Optional<Member> findMember = memberRepository.findById(request.getMemberId());

        if (findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원을 찾을 수 없습니다.");
        }
        Application application = new Application(findMember.get(), findGroup.get());
        Application saveApplication = applicationRepository.save(application);

        /* Group 에 있는 Applications 에 추가를 해준다. */
        group.addApplication(application);

        return ResponseEntity.status(HttpStatus.CREATED).body("성공적으로 모임에 신청되었습니다.");

    }

    private void checkRole(Long memberId) {

    }

    /* 모임 신청 취소 */
    public ResponseEntity<?> deleteApplication(Long applicationId, Long memberId) throws AccessDeniedException {

        /* 권한 검사 */
        checkRole(memberId);

        /* 신청서 조회 */
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("신청을 찾을 수 없습니다. ID: " + applicationId));

        /* 신청서 삭제 */
        applicationRepository.deleteById(applicationId);

        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 신청을 취소했습니다.");

    }

    /* 모임 가입 완료 */
    public ResponseEntity<?> acceptApplication(ApplicationRequestDto.applicationAccept request) {

        /* 수락에 필요한 데이터 얻어 오기 */
        Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(
                () -> new NotFoundException("옳바르지 않은 요청입니다.")
        );

        Member member = application.getMember();
        Group group = application.getGroup();

        /* 수락 버튼을 누른 사람이 관리자인가? */
        Optional<GroupMember> byGroupInAdminMember = groupMemberRepository.findByGroupInAdminMember(group.getId(), GroupRole.ADMIN, request.getGroupAdminId());

        if (byGroupInAdminMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        /* 모임에 가입 시키기 */
        GroupMember newgroupMember = new GroupMember(member, group, GroupRole.MEMBER);
        GroupMember savedGroupMember = groupMemberRepository.save(newgroupMember);

        group.getGroupMembers().add(savedGroupMember);

        /* 신청서 삭제 */
        applicationRepository.deleteById(application.getId());

        return ResponseEntity.status(HttpStatus.OK).body("모임에 가입이 완료 되었습니다.");

    }

    /* 모임 신청 수락 */
    public ResponseEntity<?> acceptedApplication(ApplicationRequestDto.applicationAccept request) {

        Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(
                () -> new NotFoundException("옳바르지 않은 요청입니다.")
        );

        Member member = application.getMember();
        Group group = application.getGroup();

        GroupMember newgroupMember = new GroupMember(member, group, GroupRole.MEMBER);
        GroupMember savedGroupMember = groupMemberRepository.save(newgroupMember);

        group.getGroupMembers().add(savedGroupMember);

        applicationRepository.deleteById(application.getId());

        return ResponseEntity.status(HttpStatus.OK).body("모임에 가입이 완료 되었습니다.");
    }

}

