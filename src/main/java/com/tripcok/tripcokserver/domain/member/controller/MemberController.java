package com.tripcok.tripcokserver.domain.member.controller;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import com.tripcok.tripcokserver.global.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    /* 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<?> createMember(@RequestBody MemberRequestDto.save request) {
        return memberService.createMember(request);
    }

    /* 회원가입 - 이매일 인증번호 전송 */
    @GetMapping("/register/{email}")
    public ResponseEntity<?> sendVerificationEmail(@PathVariable("email") String email) throws MessagingException, UnsupportedEncodingException {
        log.info(email);
        return emailService.sendVerificationEmail(email);
    }

    /* 회원가입 - 이매일 인증번호 인증 */
    @GetMapping("/register/email/check")
    public ResponseEntity<?> checkVerificationEmail(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        return emailService.verifyCode(email,code);
    }

    /* 로그인 */
    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.login request) {
        return memberService.loginMember(request);
    }

    /* 회원 정보 조회 */
//    @GetMapping("/find/{memberId}")
//    public ResponseEntity<?> getMemberInfo(@PathVariable Long memberId) {
//        return memberService.getMemberInfo(memberId);
//    }

    /* 회원 정보 수정*/
    @PutMapping("")
    public ResponseEntity<?> updateMember(@RequestBody MemberRequestDto.update request) {
        return memberService.updateMember(request);
    }

    /* 회원 삭제 */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }

    /* 회원 조회 - 복수 */
    @GetMapping("/finds/{memberId}")
    public ResponseEntity<?> getAllMemberInfo(@PathVariable Long memberId) {
        return memberService.getAllMemberInfo(memberId);
    }

}
