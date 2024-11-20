package com.tripcok.tripcokserver.domain.group.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tripcok.tripcokserver.domain.group.entity.GroupRole.ADMIN;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * - 모임 생성
     * 1. 모임을 생성
     * 2. 모임 리스트에 사용자와, 모임 추가
     */
    public GroupResponseDto createGroup(@Valid GroupRequestDto requestDto) {

        /* #1 그룹 생성 */

        //령래 추가 코드
        Board board = new Board();

        Group group = new Group(requestDto, board);

        board.addGroup(group);

        Group newGroup = groupRepository.save(group);

        Member findMember = memberRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("찾을수 없는 사용자 입니다.")
        );

        /* #2 그룹을 생성한 사람을 GroupMember에 추가 */
        GroupMember groupMember = new GroupMember(
                findMember,
                newGroup,
                ADMIN
        );

        groupMemberRepository.save(groupMember);

        GroupResponseDto responseDto = new GroupResponseDto();
        responseDto.setGroupName(newGroup.getGroupName());
        return responseDto;
    }

    // 2. 모임 조회 - 단일
    public GroupResponseDto getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));
        return new GroupResponseDto(
                group.getGroupName(),
                group.getDescription(),
                group.getCategory(),
                group.getRecruiting()
        );
    }

    // 3. 모임 조회 - 복수 (Pageable)
    public Page<Group> getGroups(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Group> all = groupRepository.findAll(pageable);
        return all;
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
                group.getRecruiting()
        );
    }

    // 4. 모임 구인상태 변경
    public void updateRecruitingStatus(Long id, Boolean recruiting) {
        // 그룹 조회
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new  EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));
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
    public void inviteMember(Long id, Long userId) {
        // 임시로 아무 동작도 하지 않음
    }

    // 11. 모임 초대 수락
    public void acceptInvite(Long id) {
        // 임시로 아무 동작도 하지 않음
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


}
