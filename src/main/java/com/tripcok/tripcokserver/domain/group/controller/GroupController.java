package com.tripcok.tripcokserver.domain.group.controller;

import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@Tag(name = "Group API", description = "샘플 API 설명")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Hello API", description = "이 API는 인사말을 반환합니다.")
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return "Hello, " + name;
    }

    /*모임 생성*/
    @Operation(summary = "모임 생성", description = "새로운 모임을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "모임이 생성되었습니다.")
    @PostMapping("/api/v1/group")
    public ResponseEntity<GroupResponseDto> createGroup(@Valid @RequestBody GroupRequestDto requestDto) {
        //requestDto에 member_id를 추가
        GroupResponseDto responseDto = groupService.createGroup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 조회 - 단일*/
    @Operation(summary = "모임 조회", description = "단일 모임을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "모임 조회 성공")
    @GetMapping("/api/v1/group/{id}")
    public ResponseEntity<GroupResponseDto> getGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroup(id));
    }

    /*모임 조회 - 복수*/
    @Operation(summary = "모임 조회", description = "모든 모임을 페이징 처리하여 조회합니다.")
    @ApiResponse(responseCode = "200", description = "모임 목록 조회 성공")
    @GetMapping("/api/v1/groups")
    public ResponseEntity<?> getGroups(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        return ResponseEntity.ok(groupService.getGroups(page, size));
    }

    /*모임 수정*/
    @Operation(summary = "모임 수정", description = "기존 모임을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "모임 수정 성공")
    @PutMapping("/api/v1/group/{id}")
    public ResponseEntity<GroupResponseDto> updateGroup(
            @PathVariable Long id, @Valid @RequestBody GroupRequestDto requestDto) {
        return ResponseEntity.ok(groupService.updateGroup(id, requestDto));
    }

    /*모임 삭제*/
    @Operation(summary = "모임 삭제", description = "모임을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "모임 삭제 성공")
    @DeleteMapping("/api/v1/group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    /*모임 구인 상태 변경*/
    @Operation(summary = "모임 구인 상태 변경", description = "모임의 구인 상태를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "모임 구인 상태 변경 성공")
    @PatchMapping("/api/v1/group/{id}/recruiting")
    public ResponseEntity<Void> updateRecruitingStatus(@PathVariable Long id, @RequestParam boolean recruiting) {
        groupService.updateRecruitingStatus(id, recruiting);
        return ResponseEntity.ok().build();
    }

    /*모임 공지 등록*/
    @Operation(summary = "모임 공지 등록", description = "모임에 공지를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "공지 등록 성공")
    @PostMapping("/api/v1/group/{id}/notices")
    public ResponseEntity<NoticeResponseDto> createNotice(
            @PathVariable Long id, @Valid @RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createNotice(id, requestDto));
    }

    /*모임 신청*/
    @Operation(summary = "모임 신청", description = "모임에 신청합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 성공")
    @PostMapping("/api/v1/group/{id}/apply")
    public ResponseEntity<Void> applyToGroup(@PathVariable Long id) {
        groupService.applyToGroup(id);
        return ResponseEntity.ok().build();
    }

    /*모임 신청 취소*/
    @Operation(summary = "모임 신청 취소", description = "모임 신청을 취소합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 취소 성공")
    @DeleteMapping("/api/v1/group/{id}/apply")
    public ResponseEntity<Void> cancelApplication(@PathVariable Long id) {
        groupService.cancelApplication(id);
        return ResponseEntity.ok().build();
    }

    /*모임 가입 완료*/
    @Operation(summary = "모임 가입 완료", description = "모임 가입을 완료합니다.")
    @ApiResponse(responseCode = "200", description = "모임 가입 완료")
    @PostMapping("/api/v1/group/{id}/join")
    public ResponseEntity<Void> completeJoin(@PathVariable Long id) {
        groupService.completeJoin(id);
        return ResponseEntity.ok().build();
    }

    /*모임 초대*/
    @Operation(summary = "모임 초대", description = "모임에 회원을 초대합니다.")
    @ApiResponse(responseCode = "200", description = "모임 초대 성공")
    @PostMapping("/api/v1/group/invite")
    public ResponseEntity<?> inviteMember(@RequestBody GroupInviteDto inviteDto) {

        return groupService.inviteMember(inviteDto);
    }

    /*모임 초대 수락*/
    @Operation(summary = "모임 초대 수락", description = "모임 초대를 수락합니다.")
    @ApiResponse(responseCode = "200", description = "모임 초대 수락 성공")
    @PostMapping("/api/v1/group/{id}/accept-invite")
    public ResponseEntity<Void> acceptInvite(@PathVariable Long id) {
        groupService.acceptInvite(id);
        return ResponseEntity.ok().build();
    }

    /*모임 멤버 추방*/
    @Operation(summary = "모임 멤버 추방", description = "모임에서 회원을 추방합니다.")
    @ApiResponse(responseCode = "200", description = "모임 멤버 추방 성공")
    @DeleteMapping("/api/v1/group/{id}/members/{userId}")
    public ResponseEntity<Void> expelMember(@PathVariable Long id, @PathVariable Long userId) {
        groupService.expelMember(id, userId);
        return ResponseEntity.ok().build();
    }

    /*모임 게시글 작성*/
    @Operation(summary = "모임 게시글 작성", description = "모임에 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @PostMapping("/api/v1/group/{id}/boards")
    public ResponseEntity<BoardResponseDto> createBoard(
            @PathVariable Long id, @Valid @RequestBody BoardRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createBoard(id, requestDto));
    }

    /*모임 게시글 댓글 작성*/
    @Operation(summary = "모임 게시글 댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    @PostMapping("/api/v1/group/{id}/boards/{boardId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long id, @PathVariable Long boardId, @Valid @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createComment(id, boardId, requestDto));
    }

    /*모임 공지사항 작성*/
    @Operation(summary = "모임 공지사항 작성", description = "모임에 공지사항을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 작성 성공")
    @PostMapping("/api/v1/group/{id}/announcements")
    public ResponseEntity<AnnouncementResponseDto> createAnnouncement(
            @PathVariable Long id, @Valid @RequestBody AnnouncementRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createAnnouncement(id, requestDto));
    }

    /*공지 알림 전송*/
    @Operation(summary = "공지 알림 전송", description = "공지사항 알림을 전송합니다.")
    @ApiResponse(responseCode = "200", description = "공지 알림 전송 성공")
    @PostMapping("/api/v1/group/{id}/send-announcement")
    public ResponseEntity<Void> sendAnnouncement(@PathVariable Long id) {
        groupService.sendAnnouncementNotification(id);
        return ResponseEntity.ok().build();
    }

    /*모임 신청 수락*/
    @Operation(summary = "모임 신청 수락", description = "모임 신청을 수락합니다.")
    @ApiResponse(responseCode = "200", description = "모임 신청 수락 성공")
    @PatchMapping("/api/v1/group/{id}/accept-application/{userId}")
    public ResponseEntity<Void> acceptApplication(@PathVariable Long id, @PathVariable Long userId) {
        groupService.acceptApplication(id, userId);
        return ResponseEntity.ok().build();
    }

    /*모임 여행지 선택*/
    @Operation(summary = "모임 여행지 선택", description = "모임의 여행지를 선택합니다.")
    @ApiResponse(responseCode = "200", description = "여행지 선택 성공")
    @PatchMapping("/api/v1/group/{id}/select-place")
    public ResponseEntity<Void> selectPlace(@PathVariable Long id, @RequestParam String place) {
        groupService.selectPlace(id, place);
        return ResponseEntity.ok().build();
    }

    /*모임 알림 조회*/
    @Operation(summary = "모임 알림 조회", description = "모임 알림을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "모임 알림 조회 성공")
    @GetMapping("/api/v1/group/{id}/notifications")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getNotifications(id));
    }

    /*모임 가입 완료*/
    @Operation(summary = "모임 가입 완료", description = "모임 가입을 완료합니다.")
    @ApiResponse(responseCode = "200", description = "모임 가입 완료")
    @PostMapping("/api/v1/group/{id}/join-completed")
    public ResponseEntity<Void> sendJoinCompletedNotification(@PathVariable Long id) {
        groupService.sendJoinCompletedNotification(id);
        return ResponseEntity.ok().build();
    }

    /*모임 가입 반려 알림*/
    @Operation(summary = "모임 가입 반려 알림", description = "모임 가입 반려 알림을 전송합니다.")
    @ApiResponse(responseCode = "200", description = "가입 반려 알림 전송 성공")
    @PostMapping("/api/v1/group/{id}/join-rejected")
    public ResponseEntity<Void> sendJoinRejectedNotification(@PathVariable Long id) {
        groupService.sendJoinRejectedNotification(id);
        return ResponseEntity.ok().build();
    }

    /*모임 공지 알림*/
    @Operation(summary = "모임 공지 알림", description = "모임 공지 알림을 전송합니다.")
    @ApiResponse(responseCode = "200", description = "공지 알림 전송 성공")
    @PostMapping("/api/v1/group/{id}/notice")
    public ResponseEntity<Void> sendNotice(@PathVariable Long id, @RequestBody NoticeRequestDto requestDto) {
        groupService.sendNoticeNotification(id, requestDto);
        return ResponseEntity.ok().build();
    }

}
