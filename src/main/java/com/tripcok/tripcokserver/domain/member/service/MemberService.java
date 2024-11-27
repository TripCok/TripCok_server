package com.tripcok.tripcokserver.domain.member.service;

import com.tripcok.tripcokserver.domain.member.dto.MemberListResponseDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberResponseDto;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());

        if (findMember.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }

        Member member = new Member(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDto.Info(memberRepository.save(member)));
    }

    /* 로그인 */
    public ResponseEntity<?> loginMember(MemberRequestDto.login request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        if (findMember.isPresent()) {
            Member member = findMember.get();
            if (member.getPassword().equals(request.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(member);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 다시 확인하세요.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입된 이메일이 없습니다.");
        }
    }

    /* 회원 정보 조회 */
    public MemberResponseDto.Info getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다. ID: " + memberId));
        /*MemberResponseDto.Info memberResponseDto = new MemberResponseDto.Info(member);*/
        return new MemberResponseDto.Info(member);
    }

    /* 회원 조회 - 복수 Pageable */
    public Page<MemberListResponseDto> getMembers(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Member> all = memberRepository.findAll(pageable);
        return all.map(MemberListResponseDto::new);
    }

    /* 회원 정보 수정 */
    public ResponseEntity<?> updateMember(MemberRequestDto.update memberRequest) {
        Optional<Member> findMember = memberRepository.findById(memberRequest.getId());
        if (findMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 회원이거나 존재하지 않는 회원입니다.");
        }

        Member updateMember = findMember.get().update(memberRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new MemberResponseDto.Info(updateMember));
    }

    /* 회원 정보 삭제 */
    public ResponseEntity<?> deleteMember(Long memberId) {
        try {
            memberRepository.deleteById(memberId);

            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 회원을 삭제 하였습니다.");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}