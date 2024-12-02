package com.tripcok.tripcokserver.domain.postcomment.controller;

import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto;
import com.tripcok.tripcokserver.domain.post.service.UnauthorizedAccessException;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.service.PostCommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    /*모임 게시글 댓글 작성*/
    @Operation(summary = "모임 게시글 댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/api/v1/postComment")
    public ResponseEntity<PostCommentResponseDto.comment> createComment(
            @Valid @RequestBody PostCommentRequestDto.comment requestDto) {

        PostCommentResponseDto.comment responseDto = postCommentService.createComment(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*댓글 조회(단일)*/
    @Operation(summary = "모임 게시글 댓글 조회", description = "게시글의 댓글을 조회합니다.")
    @GetMapping("/api/v1/postComment/{postCommentId}")
    public ResponseEntity<PostCommentResponseDto.get> getpostComment(@PathVariable Long postCommentId) {
        PostCommentResponseDto.get postCommentResponseDto = postCommentService.getPostComment(postCommentId);
        return ResponseEntity.status(HttpStatus.OK).body(postCommentResponseDto);
    }

    /*댓글 조회(복수)*/
    @Operation(summary = "모임 게시글 댓글 복수 조회", description = "게시글의 댓글을 여러개 조회합니다.")
    @GetMapping("/api/v1/postComments")
    public ResponseEntity<Page<PostCommentResponseDto.gets>> getspostComment(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("postId") Long postId
    ) {
        Page<PostCommentResponseDto.gets> pageResponseDtos = postCommentService.getPostComments(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponseDtos);
    }

    /*댓글 삭제*/
    @Operation(summary = "모임 게시글 댓글 삭제", description = "게시글의 댓글을 삭제합니다.")
    @DeleteMapping("/api/v1/postComment")
    public ResponseEntity<PostCommentResponseDto.delete> deletepostComment(@RequestBody PostCommentRequestDto.delete requestdto) throws UnauthorizedAccessException {
        PostCommentResponseDto.delete responseDto = postCommentService.deletePostCommit(requestdto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /*댓글 수정*/
    @Operation(summary = "모임 게시글 댓글 수정", description = "게시글의 댓글을 수정합니다.")
    @PutMapping("/api/v1/postComment/{postCommentId}")
    public ResponseEntity<PostCommentResponseDto.put> putpostComment(@RequestBody PostCommentRequestDto.put requestDto) throws UnauthorizedAccessException {
        PostCommentResponseDto.put responseDto = postCommentService.putPostCommit(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
