package com.tripcok.tripcokserver.domain.member.controller;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /* 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<?> createMember(@RequestBody MemberRequestDto.save request) {
        return memberService.createMember(request);
    }

    /* 로그인 */
    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.login request) {
        return memberService.loginMember(request);
    }

    /* 회원 정보 조회 */
    @GetMapping("/find/{memberId}")
    public ResponseEntity<?> getMemberInfo(@PathVariable Long memberId) {
        return memberService.getMemberInfo(memberId);
    }

    /* 회원 정보 수정*/
    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody MemberRequestDto.update request) {
        return memberService.updateMember(request);
    }

    /* 회원 삭제 */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }

    /* 회원 조회 - 복수 */
    @GetMapping("/finds")
    public ResponseEntity<?> getAllMemberInfo() {
        return memberService.getAllMemberInfo();
    }

}
