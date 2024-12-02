package com.tripcok.tripcokserver.domain.member.controller;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberResponseDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import com.tripcok.tripcokserver.global.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<?> sendVerificationEmail(@PathVariable("email") String email) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        log.info(email);
        return emailService.sendVerificationEmail(email);
    }

    /* 회원가입 - 이매일 인증번호 인증 */
    @GetMapping("/register/email/check")
    public ResponseEntity<?> checkVerificationEmail(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        return emailService.verifyCode(email, code);
    }

    /* 로그인 */
    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.login request, HttpSession session) {

        log.info(request.toString());
        return memberService.loginMember(request, session);
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

    /* 회원 정보 수정 - 프로필 이미지 변경 */
    @PutMapping("/{memberId}/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @PathVariable Long memberId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        return memberService.updateProfileImage(memberId, files);
    }

    /* 회원 정보 수정 - 프로필 이름 변경 */
    @PutMapping("/{memberId}/profile-name")
    public ResponseEntity<?> updateProfileName(
            @PathVariable Long memberId,
            @RequestParam("name") String name
    ) {
        return memberService.updateProfileName(memberId, name);
    }


    /* 회원 삭제 */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }

}
