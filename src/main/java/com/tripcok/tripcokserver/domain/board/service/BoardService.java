package com.tripcok.tripcokserver.domain.board.service;

import com.tripcok.tripcokserver.domain.board.dto.BoardRequestDto;
import com.tripcok.tripcokserver.domain.board.dto.BoardResponseDto;
import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.group.dto.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    // 13. 모임 게시글 작성
    public BoardResponseDto createBoard(Long memberId, @Valid BoardRequestDto requestDto) {
        Board board = new Board(requestDto);

        return new BoardResponseDto();
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
