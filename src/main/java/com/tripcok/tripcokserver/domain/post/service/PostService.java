package com.tripcok.tripcokserver.domain.post.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.post.dto.*;
import com.tripcok.tripcokserver.domain.postcomment.PostCommentRepository;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.post.entity.Post;

import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository boardCommentRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PostCommentRepository postCommentRepository;

    /* 게시글 생성 */
    public PostResponseDto.create createPost(Long userId, Long groupId, @Valid PostRequestDto requestDto) {

        //Notice
        log.info(requestDto.toString());

        // 사용자 조회
        Member member = findMemberById(userId);

        log.info(String.valueOf(member.getId()));

        // 그룹 및 사용자 검증
        Group group = findGroupById(groupId);
        log.info(String.valueOf(group.getId()));

        validateGroupMembership(groupId, member);

        // 그룹의 Board 가져오기
        Board board = group.getBoard();

        // 게시글 생성 및 저장
        Post post = createAndSavePost(requestDto, board);

        return new PostResponseDto.create("게시글 추가 완료", post.getId(), post.getType());
    }

    /* 공지 사항 */
    public PostResponseDto.create createNotice(Long userId, Long groupId, @Valid PostRequestDto requestDto) {

        // 사용자 조회
        Member member = findMemberById(userId);

        // 그룹 및 사용자
        Group group = findGroupById(groupId);
        validateGroupMembership(groupId, member);

        // ADMIN 검증
        Optional<GroupMember> adminMember = groupMemberRepository.findGroupMemberByMember_IdAndAndGroup_Id(userId,groupId);

        // ADMIN 권한을 가진 멤버 검증
        if (adminMember.isPresent()) {
            GroupMember groupMember = adminMember.get();

            // ADMIN 권한이 없으면 예외 발생
            if (groupMember.getRole() != GroupRole.ADMIN) {
                throw new RuntimeException("사용자가 그룹의 ADMIN 권한이 없습니다.");
            }
        } else {
            throw new RuntimeException("사용자가 해당 그룹의 멤버가 아닙니다.");
        }

        // 그룹의 Board 가져오기
        Board board = group.getBoard();
        log.info("Board ID: {}", board.getId());

        // 공지사항 생성 및 저장
        Post post = createAndSavePostandType(requestDto, board);
        return new PostResponseDto.create("공지사항 추가 완료", post.getId(),post.getType());
    }

    /* 댓글 달기 */
    public PostResponseDto.comment createComment(Long userId, Long postId, Long groupId, @Valid PostCommentRequestDto requestDto) {

        // Member 조회
        Member member = findMemberById(userId);

        // Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다. ID: " + postId));

        // 그룹 및 사용자 검증
        //Group group = findGroupById(groupId);
        validateGroupMembership(groupId, member);

        // 댓글 생성 및 저장
        PostComment postComment = new PostComment(requestDto, post, member);

        postCommentRepository.save(postComment);

        post.addComment(postComment);
        return new PostResponseDto.comment("댓글 추가 완료", postComment.getId());
    }

    private Member findMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));
    }

    private void validateGroupMembership(Long groupId, Member member) {
        List<Member> members = memberRepository.findByGroupMembersGroupId(groupId);
        if (!members.contains(member)) {
            throw new RuntimeException("사용자가 해당 그룹에 속해 있지 않습니다.");
        }
    }

    private Post createAndSavePost(@Valid PostRequestDto requestDto, Board board) {
        Post post = new Post(requestDto, board);
        board.addPosts(post);
        return postRepository.save(post);
    }
    private Post createAndSavePostandType(@Valid PostRequestDto requestDto, Board board) {
        Post post = new Post(requestDto, board);
        board.addPosts(post);
        return postRepository.save(post);
    }

    public PostResponseDto.get getPost(Long postId) {
        Optional<Post> post =  postRepository.findById(postId);

        if (post.isPresent()) {
            Post savePost = post.get();
            return new PostResponseDto.get(savePost);
        }
        else {
            throw new NullPointerException("해당 Post는 삭제되었습니다.");
        }
    }

    public Page<PostRequestDto.gets> getPosts(Pageable pageable) {

        // postRepository로 얻은 Post를 Page 객체에 넣음
        Page<Post> posts = postRepository.findAll(pageable);

        // Post 객체를 PostPageResponseDto로 변환하여 새로운 Page 객체 생성
        Page<PostRequestDto.gets> pageResponseDtos = posts.map(post ->
                new PostRequestDto.gets(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getType()
                )
        );

        log.info(pageResponseDtos.toString());
        return  pageResponseDtos;
    }

    public PostResponseDto.put putPost(Long postId, Long memberId,Long groupId, PostRequestDto.put requestDto) {
        //게시글 불러오기
        Optional<Post> post =  postRepository.findById(postId);

        // 게시글이 존재한다면
        if (post.isPresent()) {
            Post savePost = post.get();

            //만약 NOTICE라면 관리자인지 검증하는 로직
            if (requestDto.getType() == null){
                isMemberAdmin(memberId, groupId);
            }

            //변경내용을 업데이트 및 저장하는 로직
            savePost.updatePost(requestDto);
            postRepository.save(savePost);

            return new PostResponseDto.put(savePost.getId(), "post 수정 완료");
        }
        else {
            throw new NullPointerException("존재하지 않은 게시글입니다.");
        }
    }

    private boolean isMemberAdmin(Long memberId, Long groupId) {
        return groupMemberRepository.findGroupMemberByMember_IdAndAndGroup_Id(memberId, groupId)
                .map(groupMember -> groupMember.getRole().equals(GroupRole.ADMIN))
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹에 속한 멤버를 찾을 수 없습니다."));
    }

    public PostResponseDto.delete deletePost(Long postId) {
        Optional<Post> post =  postRepository.findById(postId);

        if (post.isPresent()) {
            Post savePost = post.get();
            postRepository.delete(savePost);
            return new PostResponseDto.delete(postId,"삭제 완료되었습니다.");
        }
        else {
            throw new NullPointerException("해당 Post는 존재하지 않습니다.");
        }
    }
}
