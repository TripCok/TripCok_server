package com.tripcok.tripcokserver.domain.member.controller;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberResponseDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/find/{memberId}")
    public MemberResponseDto.Info getMemberInfo(@PathVariable("memberId") Long memberId) {
        return memberService.getMemberInfo(memberId);
    }

    /* 회원 조회 - 복수 */
    @GetMapping("/finds")
    public ResponseEntity<?> getAllMembers(@RequestParam(name = "page") Integer page,
                                           @RequestParam(name = "size") Integer size,
                                           @RequestParam(name = "email") String email
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMembers(email, page, size));
    }


    /* 회원 정보 수정*/
    @PutMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@RequestBody MemberRequestDto.update request) {
        return memberService.updateMember(request);
    }

    /* 회원 삭제 */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }


}
