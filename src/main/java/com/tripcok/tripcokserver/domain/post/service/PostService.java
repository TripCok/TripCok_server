package com.tripcok.tripcokserver.domain.post.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.post.dto.PostResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.PostCommentRepository;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto;
import com.tripcok.tripcokserver.domain.post.entity.Post;

import com.tripcok.tripcokserver.domain.post.entity.Type;
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /* 게시글 생성 */
    public PostResponseDto createPost(Long userId, Long groupId, @Valid PostRequestDto requestDto) {

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

        return new PostResponseDto("게시글 추가 완료", post.getId(), post.getType());
    }

    /* 공지 사항 */
    public PostResponseDto createNotice(Long userId, Long groupId, @Valid PostRequestDto requestDto) {

        // 사용자 조회
        Member member = findMemberById(userId);

        // 그룹 및 사용자
        Group group = findGroupById(groupId);
        validateGroupMembership(groupId, member);

        // ADMIN 검증
        Optional<GroupMember> adminMember = groupMemberRepository.findGroupMemberByMember_IdAndAndGroup_Id(userId,groupId);
        System.out.println("=========================");
        System.out.println(adminMember.get().getRole());
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
        System.out.println(requestDto.getType());
        return new PostResponseDto("공지사항 추가 완료", post.getId(),post.getType());
    }

    /* 댓글 달기 */
    public PostCommentResponseDto createComment(Long userId, Long postId, Long groupId, @Valid PostCommentRequestDto requestDto) {
        // Member 조회
        Member member = findMemberById(userId);

        // Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다. ID: " + postId));

        // 그룹 및 사용자 검증
        //Group group = findGroupById(groupId);
        validateGroupMembership(groupId, member);

        // 뎃글 생성 및 저장
        PostComment boardComment = new PostComment(requestDto, post, member);

        boardCommentRepository.save(boardComment);

        post.addComment(boardComment);


        return new PostCommentResponseDto("댓글 추가 완료", boardComment.getId());
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

    private Post createAndSavePost(PostRequestDto requestDto, Board board) {
        Post post = new Post(requestDto, board);
        board.addPosts(post);
        return postRepository.save(post);
    }
    private Post createAndSavePostandType(PostRequestDto requestDto, Board board) {
        Post post = new Post(requestDto, board);
        board.addPosts(post);
        return postRepository.save(post);
    }

}
