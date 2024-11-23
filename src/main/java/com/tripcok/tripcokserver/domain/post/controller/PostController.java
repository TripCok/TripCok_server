package com.tripcok.tripcokserver.domain.post.controller;

import com.tripcok.tripcokserver.domain.post.dto.*;
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
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
    private final PostRepository postRepository;

    /*게시글 조회(단일)*/
    @GetMapping("/api/v1/post/{postId}")
    public ResponseEntity<PostResponseDto.get> getPost(@PathVariable Long postId) {
        PostResponseDto.get postResponseDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    /*게시글 조회(복수)*/
    @GetMapping("/api/v1/posts")
    public ResponseEntity<Page<PostRequestDto.gets>> getPost(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostRequestDto.gets> pageResponseDtos = postService.getPosts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponseDtos);
    }

    /*게시글 삭제*/
    @DeleteMapping("/api/v1/post/{postId}")
    public ResponseEntity<PostResponseDto.delete> deletePost(@PathVariable Long postId) {
        PostResponseDto.delete responseDto = postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /*게시글 수정*/
    @PutMapping("/api/v1/post/{postId}/member/{memberId}")
    public ResponseEntity<PostResponseDto.put> putPost( @PathVariable Long postId,
                                                       @RequestParam Long memberId, @RequestParam Long groupId,
                                                      @RequestBody PostRequestDto.put requestDto) {
        PostResponseDto.put responseDto = postService.putPost(postId, memberId, groupId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /*모임 게시글 작성*/
    @Operation(summary = "모임 게시글 작성", description = "모임에 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @PostMapping("/api/v1/user/{userId}/group/post")
    public ResponseEntity<PostResponseDto.create> createPost(
            @PathVariable("userId") Long userId,
            @RequestParam("groupId") Long groupId,
            @Valid @RequestBody PostRequestDto requestDto) {
        PostResponseDto.create responseDto = postService.createPost(userId, groupId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 게시글 댓글 작성*/
    @Operation(summary = "모임 게시글 댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    @PostMapping("/api/v1/group/{userId}/post/comment")
    public ResponseEntity<PostResponseDto.comment> createComment(
            @PathVariable("userId") Long userId,
            @RequestParam("postId") Long postId,
            @RequestParam("postId") Long groupId,
            @Valid @RequestBody PostCommentRequestDto requestDto) {

        PostResponseDto.comment responseDto = postService.createComment(userId, postId, groupId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /*모임 공지사항 작성*/
    @Operation(summary = "모임 공지사항 작성", description = "모임에 공지사항을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 작성 성공")
    @PostMapping("/api/v1/user/{userId}/group/notice")
    public ResponseEntity<PostResponseDto.create> createNotice(
            @PathVariable("userId") Long userId,
            @RequestParam("boardId") Long groupId, @Valid @RequestBody PostRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createNotice(userId, groupId, requestDto));
    }
}
