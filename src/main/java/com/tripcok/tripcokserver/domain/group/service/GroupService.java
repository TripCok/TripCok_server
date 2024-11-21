package com.tripcok.tripcokserver.domain.group.service;

import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupMemberInvite;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberInviteRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tripcok.tripcokserver.domain.group.entity.GroupRole.ADMIN;

@Service
@AllArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberInviteRepository groupMemberInviteRepository;

    /**
     * - 모임 생성
     * 1. 모임을 생성
     * 2. 모임 리스트에 사용자와, 모임 추가
     */
    public GroupResponseDto createGroup(@Valid GroupRequestDto requestDto) {

        /* #1 그룹 생성 */
        Group group = new Group(requestDto);

        Group newGroup = groupRepository.save(group);

        Member findMember = memberRepository.findById(requestDto.getMemberId()).orElseThrow(
                () -> new EntityNotFoundException("찾을수 없는 사용자 입니다.")
        );

        /* #2 그룹을 생성한 사람을 GroupMember에 추가 */
        GroupMember groupMember = new GroupMember(
                findMember,
                newGroup,
                ADMIN
        );

        groupMemberRepository.save(groupMember);

        return new GroupResponseDto(
                newGroup.getGroupName(),
                newGroup.getDescription(),
                newGroup.getCategory(),
                newGroup.isRecruiting()
        );

        /*GroupResponseDto responseDto = new GroupResponseDto();
        responseDto.setGroupName(newGroup.getGroupName());
        return responseDto;*/
    }

    // 2. 모임 조회 - 단일
    public GroupResponseDto getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));
        return new GroupResponseDto(
                group.getGroupName(),
                group.getDescription(),
                group.getCategory(),
                group.isRecruiting()
        );
    }

    // 3. 모임 조회 - 복수 (Pageable)
    public Page<GroupAllResponseDto> getGroups(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Group> all = groupRepository.findAll(pageable);
        return all.map(GroupAllResponseDto::new);
    }

    // 4. 모임 수정
    public GroupResponseDto updateGroup(Long id, @Valid GroupRequestDto requestDto) {
        // ID로 그룹 조회 (없으면 예외 발생)
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));

        // 그룹 정보 업데이트
        group.setGroupName(requestDto.getGroupName());
        group.setDescription(requestDto.getDescription());
        group.setCategory(requestDto.getCategory());

        // 업데이트된 그룹 저장!
        groupRepository.save(group);

        // 업데이트된 데이터를 DTO로 반환
        return new GroupResponseDto(
                group.getGroupName(),
                group.getDescription(),
                group.getCategory(),
                group.isRecruiting()
        );
    }

    // 4. 모임 구인상태 변경
    public void updateRecruitingStatus(Long id, Boolean recruiting) {
        // 그룹 조회
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));
        // 모임 상태 변경
        group.setRecruiting(recruiting);

        // 변경된 상태 저장
        groupRepository.save(group);
    }

    // 5. 모임 삭제
    public void deleteGroup(Long id) {
        /* 그룹 조회 */
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));
        /* 그룹 삭제 */
        groupRepository.delete(group);
    }

    // 6. 모임 공지 등록
    public NoticeResponseDto createNotice(Long id, @Valid NoticeRequestDto requestDto) {
        return new NoticeResponseDto(); // 임시 리턴
    }

    // 7. 모임 신청
    public void applyToGroup(Long id) {
        // 임시로 아무 동작도 하지 않음
    }

    // 8. 모임 신청 취소
    public void cancelApplication(Long id) {

        // 임시로 아무 동작도 하지 않음
    }

    // 9. 모임 가입 완료
    public void completeJoin(Long id) {
        // 임시로 아무 동작도 하지 않음
    }

    // 10. 모임 초대
    public ResponseEntity<?> inviteMember(GroupInviteDto groupInviteDto) {

        /* 초대한 사람의 역할 확인*/
        Optional<Member> byId = memberRepository.findById(groupInviteDto.getGroupAdminId());

        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Member sender = byId.get();

        /* 초대 받는 사람의 존재 여부 체크 */
        Optional<Member> receiver = memberRepository.findByEmail(groupInviteDto.getEmail());

        if (receiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("초대하려는 사용자를 찾을 수 없습니다.");
        }

        /* 중복 초대 방지 */
        Optional<GroupMemberInvite> byMemberIdAndGroupId = groupMemberInviteRepository.findByMember_IdAndGroup_Id(receiver.get().getId(), groupInviteDto.getGroupId());
        if (byMemberIdAndGroupId.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 초대된 사용자 입니다.");
        }

        /* 이미 모임에 가입되어 있는 멤버의 경우 초대가 가지 않음 */
        Optional<GroupMember> receiverInGroup = groupMemberRepository.findByGroup_IdAndMember_Id(groupInviteDto.getGroupId(), receiver.get().getId());

        if (receiverInGroup.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 모임에 가입되어있는 사용자입니다.");
        }

        /* Receiver의 모임에서의 권한 체크 */
        GroupMember senderInGroup = groupMemberRepository.findByGroup_IdAndMember_Id(groupInviteDto.getGroupId(), sender.getId()).orElseThrow(
                () -> new NotFoundException("옳바르지 않은 요청입니다.")
        );

        if (!senderInGroup.getRole().equals(ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("사용자를 초대할 수 있는 권환이 없습니다.");
        }

        Group group = groupRepository.findById(groupInviteDto.getGroupId()).orElseThrow(
                () -> new EntityNotFoundException("옳바르지 않은 요청입니다.")
        );

        /* 초대 받는 사람 검색 */
        GroupMemberInvite groupMemberInvite = new GroupMemberInvite(receiver.get(), group);
        groupMemberInviteRepository.save(groupMemberInvite);

        return ResponseEntity.status(HttpStatus.OK).body("모임 초대에 성공하였습니다.");
    }

    // 11. 모임 초대 수락
    public ResponseEntity<?> acceptInvite(InviteRequestDto request) {
        GroupMemberInvite invite = groupMemberInviteRepository.findById(request.getInviteId())
                .orElseThrow(() -> new EntityNotFoundException("해당 초대를 찾을 수 없습니다."));

        if (!invite.getMember().getId().equals(request.getMemberId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 초대가 초대한 멤버와 일치하지 않습니다");
        }
        /* 멤버 추가 및 초대 삭제 처리 */
        handleInviteAcceptance(invite);
        return ResponseEntity.status(HttpStatus.OK).body("모임 초대가 성공적으로 수락되었습니다.");

    }

    private void handleInviteAcceptance(GroupMemberInvite invite) {
        Group group = invite.getGroup();
        Member member = invite.getMember();

        // 2. 모임 멤버로 추가
        GroupMember newGroupMember = new GroupMember(member, group, GroupRole.MEMBER);
        groupMemberRepository.save(newGroupMember);

        // 3. 초대 행 삭제
        groupMemberInviteRepository.delete(invite);
    }


    // 12. 모임 맴버 추방
    public void expelMember(Long id, Long userId) {
        // 임시로 아무 동작도 하지 않음
    }

    // 16. 공지 알림 전송
    public void sendAnnouncementNotification(Long id) {
        // 임시로 아무 동작도 하지 않음
    }

    // 17. 모임 신청 수락
    public void acceptApplication(Long id, Long userId) {
        // 임시로 아무 동작도 하지 않음
    }

    // 18. 모임 여행지 선택
    public void selectPlace(Long id, String place) {
        // 임시로 아무 동작도 하지 않음
    }

    // 19. 모임 알림 조회
    public List<NotificationDto> getNotifications(Long id) {
        return Collections.emptyList(); // 임시 빈 리스트 리턴
    }

    // 20. 모임 가입 완료 알림 전송
    public void sendJoinCompletedNotification(Long id) {
        // 임시로 아무 동작도 하지 않음
    }

    // 21. 모임 가입 반려 알림 전송
    public void sendJoinRejectedNotification(Long id) {
        // 임시로 아무 동작도 하지 않음
    }

    // 22. 모임 공지 알림 전송
    public void sendNoticeNotification(Long id, NoticeRequestDto requestDto) {
        // 임시로 아무 동작도 하지 않음
    }


    // 이거 왜 들어가지?
    public void acceptInvite(Long inviteId, Long memberId) {
    }
}
