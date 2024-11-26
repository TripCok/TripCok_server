package com.tripcok.tripcokserver.domain.post.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.post.dto.*;
import com.tripcok.tripcokserver.domain.post.entity.Type;
import com.tripcok.tripcokserver.domain.postcomment.repository.PostCommentRepository;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.post.entity.Post;

import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PostCommentRepository postCommentRepository;

    /* 게시글 생성 */
    public PostResponseDto.create createPost(Long memberId, Long groupId, PostRequestDto.create requestDto) {
        // 사용자 조회
        Member member = findMemberById(memberId);

        log.info(String.valueOf(member.getId()));

        // 그룹 및 사용자 검증
        Group group = findGroupById(groupId);
        log.info(String.valueOf(group.getId()));

        validateGroupMembership(groupId, member);

        // 그룹의 Board 가져오기
        Board board = group.getBoard();

        // 게시글 생성 및 저장
        Post post = createAndSavePost(requestDto,Type.COMMON, board, member);

        return new PostResponseDto.create("게시글 추가 완료", post.getId(), post.getType());
    }

    private GroupRole getGroupRole(Long memberId, Long groupId) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByGroup_IdAndMember_Id(memberId, groupId);

        if (groupMember.isPresent()) {
            GroupRole role = groupMember.get().getRole();
            return role;
        }
        return null;
    }

    private void validateGroupMembership(Long groupId, Member member) {
        List<Member> members = memberRepository.findByGroupMembersGroupId(groupId);
        if (!members.contains(member)) {
            throw new RuntimeException("사용자가 해당 그룹에 속해 있지 않습니다.");
        }
    }

    /* 공지 사항 */
    public PostResponseDto.create createNotice(Long userId, Long groupId, PostRequestDto.create requestDto) {

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
        Post post = createAndSavePostandType(requestDto, Type.NOTICE, board, member);
        return new PostResponseDto.create("공지사항 추가 완료", post.getId(),post.getType());
    }


    private Member findMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));
    }

    private Post createAndSavePost(PostRequestDto.create requestDto,Type type, Board board, Member member) {
        Post post = new Post(requestDto, type, board, member);
        board.addPosts(post);
        return postRepository.save(post);
    }
    private Post createAndSavePostandType(PostRequestDto.create requestDto,Type type, Board board, Member member) {
        Post post = new Post(requestDto, type, board, member);
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

    public Page<PostResponseDto.gets> getPosts(Pageable pageable) {

        // postRepository로 얻은 Post를 Page 객체에 넣음
        Page<Post> posts = postRepository.findAll(pageable);

        // Post 객체를 PostPageResponseDto로 변환하여 새로운 Page 객체 생성
        Page<PostResponseDto.gets> pageResponseDtos = posts.map(post ->
                new PostResponseDto.gets(
                        post.getTitle(),
                        post.getContent(),
                        post.getType()
                )
        );

        log.info(pageResponseDtos.toString());
        return pageResponseDtos;
    }

    public PostResponseDto.put putPost(Long postId, Long memberId, Long groupId, PostRequestDto.put requestDto) throws UnauthorizedAccessException {
        //게시글 불러오기
        Optional<Post> post =  postRepository.findById(postId);

        // 게시글이 존재한다면
        if (post.isPresent()) {
            Post savePost = post.get();

            //만약 NOTICE라면 관리자인지 검증하는 로직
            if (requestDto.getType() == null){
                isMemberAdmin(memberId, groupId);
                isWriter(memberId, savePost);
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

    private Boolean isWriter(Long memberId, Post post) throws UnauthorizedAccessException {
        if (!post.getMember().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("작성자가 아니어서 수정할 수 없습니다.");
        }
        return true;
    }

    private boolean isMemberAdmin(Long memberId, Long groupId) {
        return groupMemberRepository.findGroupMemberByMember_IdAndAndGroup_Id(memberId, groupId)
                .map(groupMember -> groupMember.getRole().equals(GroupRole.ADMIN))
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹에 속한 멤버를 찾을 수 없습니다."));
    }

    public PostResponseDto.delete deletePost(Long postId, Long memberId) throws UnauthorizedAccessException {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원은 존재하지 않습니다."));

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 Post는 존재하지 않습니다."));

        // 작성자 확인
        isWriter(memberId, post);

        // 게시글 삭제
        postRepository.delete(post);

        // 성공 응답 반환
        return new PostResponseDto.delete(postId, "삭제 완료되었습니다.");
    }

}
