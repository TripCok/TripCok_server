package com.tripcok.tripcokserver.domain.member.controller;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관리와 관련된 API")
public class MemberController {

    private final MemberService memberService;

    /* 회원가입 */
    @Operation(summary = "회원가입", description = "회원가입을 처리합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> createMember(@RequestBody MemberRequestDto.save request) {
        return memberService.createMember(request);
    }

    /* 로그인 */
    @Operation(summary = "로그인", description = "회원 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.login request) {
        return memberService.loginMember(request);
    }

    /* 회원 정보 조회 */
    @Operation(summary = "회원 정보 조회", description = "회원 ID를 기반으로 회원 정보를 조회합니다.")
    @GetMapping("/find/{memberId}")
    public ResponseEntity<?> getMemberInfo(@PathVariable Long memberId) {
        return memberService.getMemberInfo(memberId);
    }

    /* 회원 정보 수정 */
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable("memberId") Long memberId, @RequestBody MemberRequestDto.update request) {
        return memberService.updateMember(memberId, request);
    }

    /* 회원 삭제 */
    @Operation(summary = "회원 삭제", description = "회원 ID를 기반으로 회원을 삭제합니다.")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }

    /* 회원 조회 - 복수 */
    @Operation(summary = "회원 조회 - 복수", description = "특정 조건에 따라 여러 회원 정보를 조회합니다.")
    @GetMapping("/finds")
    public ResponseEntity<?> getAllMemberInfo(@PathVariable Long memberId) {
        return memberService.getAllMemberInfo(memberId);
    }
}