package com.tripcok.tripcokserver.domain.post.controller;

import com.tripcok.tripcokserver.domain.post.dto.PostResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto;
import com.tripcok.tripcokserver.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    /*모임 게시글 작성*/
    @Operation(summary = "모임 게시글 작성", description = "모임에 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @PostMapping("/api/v1/user/{userId}/group/post")
    public ResponseEntity<PostResponseDto> createPost(
            @PathVariable("userId") Long userId,
            @RequestParam("groupId") Long groupId,
            @Valid @RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.createPost(userId, groupId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 게시글 댓글 작성*/
    @Operation(summary = "모임 게시글 댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    @PostMapping("/api/v1/group/{userId}/post/comment")
    public ResponseEntity<
            PostCommentResponseDto> createComment(
            @PathVariable("userId") Long userId,
            @RequestParam("postId") Long postId,
            @RequestParam("postId") Long groupId,
            @Valid @RequestBody PostCommentRequestDto requestDto) {

        PostCommentResponseDto responseDto = postService.createComment(userId, postId, groupId, requestDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 공지사항 작성*/
    @Operation(summary = "모임 공지사항 작성", description = "모임에 공지사항을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 작성 성공")
    @PostMapping("/api/v1/user/{userId}/group/notice")
    public ResponseEntity<PostResponseDto> createNotice(
            @PathVariable("userId") Long userId,
            @RequestParam("boardId") Long groupId, @Valid @RequestBody PostRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createNotice(userId, groupId, requestDto));
    }
}