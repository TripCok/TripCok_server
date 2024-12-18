package com.tripcok.tripcokserver.domain.member.controller;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberResponseDto;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
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
    public ResponseEntity<?> createMember(@RequestBody MemberRequestDto.save request, HttpSession session) {
        return memberService.createMember(request, session);
    }

    /* 회원가입 - 이매일 인증번호 전송 */
    @GetMapping("/register/{email}")
    public ResponseEntity<?> sendVerificationEmail(@PathVariable("email") String email, HttpSession session) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        log.info(email);
        return emailService.sendVerificationEmail(email, session);
    }

    /* 회원가입 - 이매일 인증번호 인증 */
    @GetMapping("/register/email/check")
    public ResponseEntity<?> checkVerificationEmail(
            @RequestParam("email") String email,
            @RequestParam("code") String code,
            HttpSession session
    ) {
        return emailService.verifyCode(email, code, session);
    }

    /* 로그인 */
    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.login request, HttpSession session) {
        log.info(request.toString());
        return memberService.loginMember(request, session);
    }

    /* AsyncLogin */
    @PutMapping("/login/async")
    public ResponseEntity<?> login(@RequestParam Long id, @RequestParam String email, HttpSession session) {

        return memberService.asyncLogin(id, email, session);

    }

    @PutMapping("/prefer/category")
    public ResponseEntity<?> login(
            @RequestParam List<Long> categoryIds, HttpSession session) {
        return memberService.setPreferCategory(categoryIds, session);
    }

    /* TODO
    * - prefer Skip 처리
    * */

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
