package com.tripcok.tripcokserver.domain.postcomment.service;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto;
import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import com.tripcok.tripcokserver.domain.post.service.UnauthorizedAccessException;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.repository.PostCommentRepository;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /* 댓글 달기 */
    public PostCommentResponseDto.comment createComment(Long userId, Long postId, Long groupId, PostCommentRequestDto.comment requestDto) {

        // Member 조회
        Member member = findMemberById(userId);

        // Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다. ID: " + postId));

        // 댓글 생성 및 저장
        PostComment postComment = new PostComment(requestDto, post, member);

        postCommentRepository.save(postComment);

        post.addComment(postComment);

        return new PostCommentResponseDto.comment("댓글 추가 완료", postComment.getId());
    }


    public PostCommentResponseDto.get getPostComment(Long postCommentId) {
        Optional<PostComment> postComment = postCommentRepository.findById(postCommentId);

        if (postComment.isPresent()) {
            PostComment comment = postComment.get();
            return new PostCommentResponseDto.get(comment);
        }else{
            throw new NullPointerException("해당 댓글은 삭제되었습니다");
        }
    }

    public Page<PostCommentResponseDto.gets> getPostComments(Pageable pageable) {
        Page<PostComment> postComments = postCommentRepository.findAll(pageable);

        // PostComment 객체를 PostCommentPageResponseDto로 변환하여 새로운 Page 객체 생성
        Page<PostCommentResponseDto.gets> pageResponseDtos = postComments.map(postComment ->
                new PostCommentResponseDto.gets(
                        postComment.getContent())
                );
        return pageResponseDtos;
    }


    public PostCommentResponseDto.delete deletePostCommit(Long postCommentId, Long memberId) throws UnauthorizedAccessException {

        // 게시글 조회
        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글은 존재하지 않습니다."));

        // 작성자 확인
        isWriter(memberId, postComment);

        // 게시글 삭제
        postCommentRepository.delete(postComment);

        // 성공 응답 반환
        return new PostCommentResponseDto.delete(postCommentId, "삭제 완료되었습니다.");
    }

    public PostCommentResponseDto.put putPostCommit(Long postCommentId, Long memberId, Long groupId, PostCommentRequestDto.put requestDto) throws UnauthorizedAccessException {
        //댓글 불러오기
        Optional<PostComment> postComment =  postCommentRepository.findById(postCommentId);

        // 댓글이 존재한다면
        if (postComment.isPresent()) {
            PostComment savePostComment = postComment.get();

            // 작성자 확인
            isWriter(memberId, savePostComment);

            //변경내용을 업데이트 및 저장하는 로직
            savePostComment.updatePostComment(requestDto);
            postCommentRepository.save(savePostComment);

            return new PostCommentResponseDto.put(savePostComment.getId(), "post 수정 완료");
        }
        else {
            throw new NullPointerException("존재하지 않은 게시글입니다.");
        }
    }


    private boolean isWriter(Long memberId, PostComment postComment) throws UnauthorizedAccessException {
        if (!postComment.getMember().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("작성자가 아니어서 수정할 수 없습니다.");
        }
        return true;
    }

    private Member findMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

}
