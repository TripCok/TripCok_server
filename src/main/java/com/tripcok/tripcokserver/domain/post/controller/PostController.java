package com.tripcok.tripcokserver.domain.post.controller;

import com.tripcok.tripcokserver.domain.post.dto.*;
import com.tripcok.tripcokserver.domain.post.service.UnauthorizedAccessException;
import com.tripcok.tripcokserver.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class PostController {

    private final PostService postService;

    /*게시글 조회(단일)*/
    @GetMapping("/api/v1/post/{postId}")
    public ResponseEntity<PostResponseDto.get> getPost(@PathVariable Long postId) {
        PostResponseDto.get postResponseDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    /*게시글 조회(복수)*/
    @GetMapping("/api/v1/posts")
    public ResponseEntity<?> getPost(
            @RequestParam("groupId") Long groupId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPosts(groupId, pageable));
    }

    /*게시글 삭제*/
    @DeleteMapping("/api/v1/post/{postId}")
    public ResponseEntity<PostResponseDto.delete> deletePost(@PathVariable Long postId, @RequestParam Long memberId) throws UnauthorizedAccessException {
        PostResponseDto.delete responseDto = postService.deletePost(postId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /*게시글 수정*/
    @PutMapping("/api/v1/post/{postId}")
    public ResponseEntity<PostResponseDto.put> putPost(@PathVariable Long postId,
                                                       @RequestParam Long memberId, @RequestParam Long groupId,
                                                       @RequestBody PostRequestDto.put requestDto) throws UnauthorizedAccessException {
        PostResponseDto.put responseDto = postService.putPost(postId, memberId, groupId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /*모임 게시글 작성*/
    @Operation(summary = "모임 게시글 작성", description = "모임에 게시글을 작성합니다.")
    @PostMapping("/api/v1/group/post")
    public ResponseEntity<PostResponseDto.create> createPost(
            @RequestParam("memberId") Long memberId,
            @RequestParam("groupId") Long groupId,
            @Valid @RequestBody PostRequestDto.create requestDto) {
        PostResponseDto.create responseDto = postService.createPost(memberId, groupId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 공지사항 작성*/
    @Operation(summary = "모임 공지사항 작성", description = "모임에 공지사항을 작성합니다.")
    @PostMapping("/api/v1/group/notice")
    public ResponseEntity<PostResponseDto.create> createNotice(
            @RequestParam("member") Long memberId,
            @RequestParam("boardId") Long groupId, @Valid @RequestBody PostRequestDto.create requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createNotice(memberId, groupId, requestDto));
    }
}
