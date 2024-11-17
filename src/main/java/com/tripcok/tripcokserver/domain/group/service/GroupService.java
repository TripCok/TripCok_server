package com.tripcok.tripcokserver.domain.group.service;

import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;

import static com.tripcok.tripcokserver.domain.group.entity.GroupRole.ADMIN;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    /**
     * - 모임 생성
     * 1. 모임을 생성
     * 2. 모임 리스트에 사용자와, 모임 추가
     * */
    public GroupResponseDto createGroup(@Valid GroupRequestDto requestDto) {

        /* #1 그룹 생성 */
        Group group = new Group(requestDto);

        Group newGroup = groupRepository.save(group);

        /*
         * 나중에 하람쓰가 추가 하면 대체 해야할 부분
         * How?
         * 1. 하람쓰가 만든 Repository를 여기 위에 추가한다.
         * 2. Member member = memberRepository.findById(requestDto.getUserId);
         * Ok?
         * */
        Member member = new Member();

        /* #2 그룹을 생성한 사람을 GroupMember에 추가 */
        GroupMember groupMember = new GroupMember(
                member,
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
        return new GroupResponseDto(); // 임시 리턴
    }

    // 3. 모임 조회 - 복수 (Pageable)
    public Page<GroupResponseDto> getGroups(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList()); // 임시 빈 페이지 리턴
    }

    // 4. 모임 수정
    public GroupResponseDto updateGroup(Long id, @Valid GroupRequestDto requestDto) {
        return new GroupResponseDto(); // 임시 리턴
    }

    // 5. 모임 삭제
    public void deleteGroup(Long id) {
        // 임시로 아무 동작도 하지 않음
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

    // 13. 모임 게시글 작성
    public BoardResponseDto createBoard(Long id, @Valid BoardRequestDto requestDto) {
        return new BoardResponseDto();
    }

    // 14. 모임 게시물 댓글 작성
    public CommentResponseDto createComment(Long id, Long postId, @Valid CommentRequestDto requestDto) {
        return new CommentResponseDto(); // 임시 리턴
    }

    // 15. 모임 공지사항 작성
    public AnnouncementResponseDto createAnnouncement(Long id, @Valid AnnouncementRequestDto requestDto) {
        return new AnnouncementResponseDto(); // 임시 리턴
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
