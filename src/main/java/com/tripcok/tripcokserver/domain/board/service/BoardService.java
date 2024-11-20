package com.tripcok.tripcokserver.domain.board.service;

import com.tripcok.tripcokserver.domain.board.dto.BoardRequestDto;
import com.tripcok.tripcokserver.domain.board.dto.BoardResponseDto;
import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.board.repository.BoardRepository;
import com.tripcok.tripcokserver.domain.boardcomment.dto.BoardCommentRequestDto;
import com.tripcok.tripcokserver.domain.boardcomment.dto.BoardCommentResponseDto;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;

import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BoardService {

    private static MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final BoardRepository boardRepository;

    public BoardService(MemberRepository memberRepository,GroupRepository groupRepository, BoardRepository boardRepository) {
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
        this.boardRepository = boardRepository;
    }

    // 13. 모임 게시글 작성
    public BoardResponseDto createBoard(Long userId, Long groupId, @Valid BoardRequestDto requestDto) {

        // 1. 게시글을 특정 그룹에 소속시킴
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모임입니다."));

        Member writerUser= memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. Board 엔티티 생성 및 저장
        Board board = new Board(requestDto, group, writerUser);

        // 3. Board 정보 저장
        Optional<Board> savedBoard = Optional.of(boardRepository.save(board));

        // 4.responseDto 객체 생성
        BoardResponseDto responseDto = new BoardResponseDto();

        // 4. 저장 결과에 따라 로직 처리
        if (savedBoard.isPresent()) {
            // 정보 저장 성공
            responseDto.save(savedBoard.get().getTitle(), "저장 완료");
            return responseDto;
        } else {
            // 정보 저장 실패
            responseDto.save("저장 실패", "저장 실패");
            return responseDto;
        }
    }

    // 14. 모임 게시물 댓글 작성
    public BoardCommentResponseDto createComment(Long id, Long postId, @Valid BoardCommentRequestDto requestDto) {

        return new BoardCommentResponseDto(); // 임시 리턴
    }

    // 15. 모임 공지사항 작성
    public AnnouncementResponseDto createAnnouncement(Long id, @Valid AnnouncementRequestDto requestDto) {
        return new AnnouncementResponseDto(); // 임시 리턴
    }

}
