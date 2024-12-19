package com.tripcok.tripcokserver.domain.postcomment.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.dto.GroupRequestDto;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto;
import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import com.tripcok.tripcokserver.domain.post.service.PostService;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import com.tripcok.tripcokserver.domain.postcomment.repository.PostCommentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class PostCommentPerformanceTest {
//
//    private Member member;
//
//    private Group group;
//
//    private GroupMember groupMember;
//
//    private Board board;
//
//    private Post post;
//
//    private PostComment postComment;
//
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private GroupRepository groupRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private GroupMemberRepository groupMemberRepository;
//    @Autowired
//    private PostCommentService postCommentService;
//    @Autowired
//    private PostCommentRepository postCommentRepository;
//
//    @BeforeEach()
//    public void setData() {
//        // 회원이여야 함
//        MemberRequestDto.save memberRequestDto = new MemberRequestDto.save();
//        memberRequestDto.setName("test");
//        memberRequestDto.setEmail("test@test.com");
//        memberRequestDto.setPassword("123");
//        this.member = new Member(memberRequestDto);
//
//        memberRepository.save(member);
//
//        // 모임이여야 함
//        GroupRequestDto groupRequestDto = new GroupRequestDto();
//        groupRequestDto.setGroupName("test");
//        groupRequestDto.setCategory("test");
//        groupRequestDto.setMemberId(member.getId());
//
//        //Group 생성과 동시에 Board 생성
//        this.board = new Board();
//
//        this.group = new Group(groupRequestDto, board);
//
//        groupRepository.save(group);
//
//        this.groupMember = new GroupMember(member, group, GroupRole.MEMBER);
//
//        groupMemberRepository.save(groupMember);
//
//        this.board = group.getBoard();
//
//        System.out.println("this Board : " + board);
//
//        PostRequestDto.create requestDto = new PostRequestDto.create();
//        requestDto.setTitle("test");
//        requestDto.setContent("test");
//        requestDto.setContent("test2");
//
//        this.post = new Post(requestDto, this.board, member);
//
//        postRepository.save(this.post);
//
//        board.addPosts(this.post);
//
//        PostCommentRequestDto.comment pcrequestDto = new PostCommentRequestDto.comment();
//        pcrequestDto.setPostId(this.post.getId());
//        pcrequestDto.setContent("test");
//
//        this.postComment = new PostComment(pcrequestDto, post, member);
//        postCommentRepository.save(this.postComment);
//    }
//
//    private static final String[] sampleComments = {
//            "이 게시글 정말 유용했어요!",
//            "좋은 정보 감사합니다.",
//            "정말 멋진 글이에요!",
//            "이 부분을 더 설명해주시면 좋겠어요.",
//            "좋은 아이디어네요!",
//            "정확히 이해했어요. 감사합니다.",
//            "다음 글도 기대됩니다!"
//    };
//
//    public void generatePostComments(int numberOfComments) {
//        List<PostComment> comments = new ArrayList<>();
//
//        for (int i = 0; i < numberOfComments; i++) {
//            PostCommentRequestDto.comment pcrequestDto = new PostCommentRequestDto.comment();
//            pcrequestDto.setPostId(this.post.getId());
//            pcrequestDto.setContent(generateRandomComment());
//
//            PostComment comment = new PostComment(pcrequestDto, post, member);
//            postCommentRepository.save(comment);
//        }
//
//        postCommentRepository.saveAll(comments);  // 대량으로 댓글 저장
//    }
//
//    // 랜덤으로 댓글 내용 생성
//    private String generateRandomComment() {
//        Random random = new Random();
//        int index = random.nextInt(sampleComments.length);
//        return sampleComments[index];
//    }
//
//    @Test
//    @Timeout(value = 2, unit = TimeUnit.SECONDS)  // 테스트가 2초를 초과하면 실패
//    public void testFetchPostWithCommentsPerformance() {
//
//        long startTime = System.nanoTime();
//
//        //3000개 생성 후 조회 -> 3000개까지 2초 이내
//        generatePostComments(10);
//
//        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
//
//        Page<PostCommentResponseDto.gets> comments = postCommentService.getPostComments(pageable);
//
//        long endTime = System.nanoTime();  // 성능 측정을 위한 종료 시간
//
//        long duration = endTime - startTime;  // 성능 측정 (단위: 나노초)
//
//        System.out.println("조회된 댓글 수: " + comments.getContent().size());
//        System.out.println("성능 측정 시간: " + duration / 1_000_000 + " ms");
//
//        // 성능이 2초 이내로 실행됐는지 확인
//        assertTrue(duration < 2_000_000_000, "성능이 5초를 초과했습니다.");
//    }

}
