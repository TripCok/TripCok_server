package com.tripcok.tripcokserver.domain.member.service;

import com.tripcok.tripcokserver.domain.file.FileDto;
import com.tripcok.tripcokserver.domain.file.service.FileService;
import com.tripcok.tripcokserver.domain.member.dto.MemberListResponseDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.dto.MemberResponseDto;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Value("${save.location.user.profile}")
    private String savePathDir;

    /* 회원가입 */
    @Transactional
    public ResponseEntity<?> createMember(MemberRequestDto.save request, HttpSession session) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());

        if (findMember.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }

        Member member = new Member(request);

        Member saveMember = memberRepository.save(member);

        session.setAttribute("member", saveMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDto.Info(saveMember));
    }

    /* 로그인 */
    public ResponseEntity<?> loginMember(MemberRequestDto.login request, HttpSession session) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        if (findMember.isPresent()) {
            Member member = findMember.get();
            if (member.getPassword().equals(request.getPassword())) {
                session.setAttribute("member", member);
                return ResponseEntity.status(HttpStatus.OK).body(new MemberResponseDto.Info(member));
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
    public Page<MemberListResponseDto> getMembers(String query, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        log.info(query + " : " + pageNum + " : " + pageSize);

        if (query == null || query.isEmpty()) {
            return memberRepository.findAll(pageable).map(MemberListResponseDto::new);
        }

        return memberRepository.findByNameContainingOrEmailContaining(query, query, pageable)
                .map(MemberListResponseDto::new);
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

    /**
     * 회원 프로필 이미지 업데이트
     */
    @Transactional()
    public ResponseEntity<?> updateProfileImage(Long memberId, List<MultipartFile> files) {
        // 멤버 확인 및 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("올바르지 않은 요청입니다."));

        try {
            // 기존 프로필 이미지 삭제
            deleteExistingProfileImage(member);

            // 새로운 프로필 이미지 저장
            FileDto fileDto = saveNewProfileImage(files);

            // 멤버 프로필 이미지 업데이트
            member.updateProfileImage(fileDto.getPath());

            return ResponseEntity.status(HttpStatus.OK).body(fileDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 이유로 업데이트에 실패하였습니다.");
        }
    }

    /**
     * 기존 프로필 이미지를 삭제합니다.
     */
    private void deleteExistingProfileImage(Member member) {
        if (member.getProfileImage() != null) {
            boolean isDeleted = fileService.deleteFile(member.getProfileImage());
            if (!isDeleted) {
                throw new IllegalStateException("기존 프로필 이미지를 삭제하지 못했습니다.");
            }
        }
    }

    /**
     * 새 프로필 이미지를 저장하고 경로를 반환합니다.
     */
    private FileDto saveNewProfileImage(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        List<FileDto> fileDtoList = fileService.saveFiles(files, System.getProperty("user.home") + savePathDir);
        if (fileDtoList.size() != 1) {
            throw new IllegalArgumentException("여러 장의 사진은 저장할 수 없습니다.");
        }

        return fileDtoList.get(0);
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


    /* 회원 정보 수정 - 이름*/
    @Transactional
    public ResponseEntity<?> updateProfileName(Long memberId, String name) {
        try {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("옳바르지 않은 요청입니다."));
            member.updateName(name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 업데이트 하였습니다.");
    }


    public ResponseEntity<?> asyncLogin(Long id, String email, HttpSession session) {
        Optional<Member> byIdAndEmail = memberRepository.findByIdAndEmail(id, email);

        if (byIdAndEmail.isPresent()) {

            session.setAttribute("member", new MemberResponseDto.Info(byIdAndEmail.get()));
            return ResponseEntity.status(HttpStatus.OK).body(new MemberResponseDto.Info(byIdAndEmail.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("옳바르지 않은 요청입니다.");
        }
    }
}