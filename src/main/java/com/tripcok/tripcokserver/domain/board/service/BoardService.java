package com.tripcok.tripcokserver.domain.board.service;

import com.tripcok.tripcokserver.domain.board.dto.BoardRequestDto;
import com.tripcok.tripcokserver.domain.board.dto.BoardResponseDto;
import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.member.entity.Member;

import com.tripcok.tripcokserver.domain.group.dto.*;
import jakarta.validation.Valid;
import org.hibernate.metamodel.internal.MemberResolver;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    private static MemberRepository memberRepository;
    // 13. 모임 게시글 작성
    public BoardResponseDto createBoard(Long memberId, @Valid BoardRequestDto requestDto) {
        // 1. memberId로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. Board 엔티티 생성 및 저장
        Board board = new Board(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getGroupId(),
                member
        );

        Board savedBoard = boardRepository.save(board);

        // 3. Response DTO 생성 및 반환
        return new BoardResponseDto(
                savedBoard.getId(),
                savedBoard.getTitle(),
                savedBoard.getContent(),
                savedBoard.getGroupId(),
                savedBoard.getMember().getId()
        );
    }

    // 14. 모임 게시물 댓글 작성
    public CommentResponseDto createComment(Long id, Long postId, @Valid CommentRequestDto requestDto) {
        return new CommentResponseDto(); // 임시 리턴
    }

    // 15. 모임 공지사항 작성
    public AnnouncementResponseDto createAnnouncement(Long id, @Valid AnnouncementRequestDto requestDto) {
        return new AnnouncementResponseDto(); // 임시 리턴
    }
}
