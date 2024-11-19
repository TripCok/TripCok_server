package com.tripcok.tripcokserver.domain.member.service;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberResponseDto;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /* 회원가입 */
    @Transactional
    public ResponseEntity<?> createMember(MemberRequestDto.save request) {
        // 이메일로 회원 검색
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        if (findMember.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }

        // 회원 생성 및 저장
        Member member = new Member(request);
        Member savedMember = memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    /* 로그인 */
    public ResponseEntity<?> loginMember(MemberRequestDto.login request) {
        // 이메일로 회원 검색
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        if (findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입된 이메일이 없습니다.");
        }

        Member member = findMember.get();
        // 비밀번호 검증
        if (!member.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 다시 확인하세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    /* 회원 정보 조회 */
    public ResponseEntity<?> getMemberInfo(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 회원이거나 존재하지 않는 회원입니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(findMember.get());
    }

    /* 회원 정보 수정 */
    @Transactional
    public ResponseEntity<?> updateMember(Long memberId, MemberRequestDto.update memberRequest) {

        // ID로 회원 검색
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 회원이거나 존재하지 않는 회원입니다.");
        }

        // 회원 정보 업데이트
        Member member = findMember.get();
        Member updatedMember = member.update(memberRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new MemberResponseDto.Info(updatedMember));
    }

    /* 회원 정보 삭제 */
    @Transactional
    public ResponseEntity<?> deleteMember(Long memberId) {
        try {
            // 회원 삭제
            memberRepository.deleteById(memberId);
            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 회원을 삭제 하였습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /* 회원 조회 - 복수 */
    public ResponseEntity<?> getAllMemberInfo(Long memberId) {
        // 모든 회원 조회
        return ResponseEntity.status(HttpStatus.OK).body(memberRepository.findAll());
    }
}
