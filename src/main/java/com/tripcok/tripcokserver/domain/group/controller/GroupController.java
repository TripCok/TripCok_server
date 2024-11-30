package com.tripcok.tripcokserver.domain.group.controller;

import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Group API", description = "샘플 API 설명")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {

        this.groupService = groupService;
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
    @PostMapping("/api/v1/group/accept-invite")
    public ResponseEntity<Void> acceptInvite(@RequestBody InviteRequestDto request) {
        groupService.acceptInvite(request);
        return ResponseEntity.ok().build();
    }
}
