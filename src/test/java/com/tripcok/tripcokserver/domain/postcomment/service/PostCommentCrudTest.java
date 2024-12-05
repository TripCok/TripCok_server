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
import com.tripcok.tripcokserver.domain.post.dto.PostResponseDto;
import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.domain.post.entity.Type;
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import com.tripcok.tripcokserver.domain.post.service.PostService;
import com.tripcok.tripcokserver.domain.post.service.UnauthorizedAccessException;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentRequestDto;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import com.tripcok.tripcokserver.domain.postcomment.repository.PostCommentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class PostCommentCrudTest {

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
//        this.post = new Post(requestDto, Type.COMMON, this.board, member);
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
//    @Test
//    public void getPostComment(){
//
//        // 생성한 댓글 정보와 조회한 댓글이 같아야 함
//        String content = this.postComment.getContent();
//
//        PostCommentResponseDto.get responseDto = postCommentService.getPostComment(postComment.getId());
//
//        Assertions.assertEquals(content, responseDto.getContent());
//    }
//
//    @Test
//    public void getPosts(){
//        //Given
//        Pageable pageable = PageRequest.of(0,2, Sort.by("id").ascending());
//
//        //when
//        Page<PostCommentResponseDto.gets> pages = postCommentService.getPostComments(pageable);
//
//        //when
//        Assertions.assertNotNull(pages);
//    }
//
//    @Test
//    public void putPostComment() throws UnauthorizedAccessException{
//        //dto로 넣은 값과 조회한 값이 일치하는지 여부
//
//        //Given
//        Long postCommentId = this.postComment.getId();
//
//        PostCommentRequestDto.put requestDto = new PostCommentRequestDto.put();
//        requestDto.setContent("test");
//
//        //when
//        PostCommentResponseDto.put reseponse = postCommentService.putPostCommit(requestDto);
//
//        //then
//        Assertions.assertNotNull(reseponse);
//        Assertions.assertEquals(postCommentId, reseponse.getId());
//    }
//
//    @Test
//    public void deletePostComment() throws UnauthorizedAccessException{
//        //given
//        Long postCommentId = this.postComment.getId();
//        Long memberId = this.member.getId();
//
//        //when
//        postCommentService.deletePostCommit(postCommentId,memberId);
//
//        //then
//        Assertions.assertThrows(Exception.class, () -> {
//            postCommentRepository.findById(postCommentId).get().getId();
//        });
//    }
}
