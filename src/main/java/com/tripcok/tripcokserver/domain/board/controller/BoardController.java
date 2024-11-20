package com.tripcok.tripcokserver.domain.board.controller;

import com.tripcok.tripcokserver.domain.board.dto.BoardRequestDto;
import com.tripcok.tripcokserver.domain.board.dto.BoardResponseDto;
import com.tripcok.tripcokserver.domain.board.service.BoardService;
import com.tripcok.tripcokserver.domain.boardcomment.dto.BoardCommentRequestDto;
import com.tripcok.tripcokserver.domain.boardcomment.dto.BoardCommentResponseDto;
import com.tripcok.tripcokserver.domain.group.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /*모임 게시글 작성*/
    @Operation(summary = "모임 게시글 작성", description = "모임에 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @PostMapping("/api/v1/user/{userId}/group/{groupId}/boards")
    public ResponseEntity<BoardResponseDto> createBoard(
            @PathVariable("userId") Long userId,
            @PathVariable("groupId") Long groupId,
            @Valid @RequestBody BoardRequestDto requestDto) {

        BoardResponseDto responseDto = boardService.createBoard(userId, groupId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 게시글 댓글 작성*/
    @Operation(summary = "모임 게시글 댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    @PostMapping("/api/v1/group/{userId}/boards/{boardId}/comments")
    public ResponseEntity<BoardCommentResponseDto> createComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId, @Valid @RequestBody BoardCommentRequestDto requestDto) {

            BoardCommentResponseDto responseDto = boardService.createComment(userId, boardId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createComment(userId, boardId, requestDto));
    }

    /*모임 공지사항 작성*/
    @Operation(summary = "모임 공지사항 작성", description = "모임에 공지사항을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 작성 성공")
    @PostMapping("/api/v1/group/{id}/announcements")
    public ResponseEntity<AnnouncementResponseDto> createAnnouncement(
            @PathVariable Long id, @Valid @RequestBody AnnouncementRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createAnnouncement(id, requestDto));
    }
}
