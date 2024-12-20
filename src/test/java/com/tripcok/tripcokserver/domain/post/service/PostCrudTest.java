package com.tripcok.tripcokserver.domain.post.service;

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
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostCrudTest {

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
//
//    @BeforeEach()
//    public void setData(){
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
//    }
//
//    @Test
//    public void getPost(){
//
//        // 생성한 게시글(Post) 정보와 조회한 게시글(Post)가 같아야 함
//        Long postId = this.post.getId();
//
//        System.out.println("postId = " + postId);
//
//        PostResponseDto.get responseDto = postService.getPost(postId);
//
//        Assertions.assertEquals(postId, responseDto.getId());
//    }
//
//    @Test
//    public void getPosts(){
//
//        //Given
//        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
//
//        //when
//        Page<PostResponseDto.gets> pages = postService.getPosts(pageable);
//
//        //then
//        Assertions.assertNotNull(pages);
//    }
//
//    @Test
//    public void putPost() throws UnauthorizedAccessException {
//        //dto로 넣은 값과 조회한 값이 일치하는 지 여부
//        //Given
//        Long postId = this.post.getId();
//        Long memberId = this.member.getId();
//        Long groupId = this.group.getId();
//        PostRequestDto.put requestDto = new PostRequestDto.put();
//        requestDto.setTitle("test");
//        requestDto.setContent("test");
//
//        //when
//        PostResponseDto.put response = postService.putPost(postId, memberId, groupId, requestDto);
//
//        //then
//        Assertions.assertNotNull(response);
//        Assertions.assertEquals(postId, response.getId());
//        Assertions.assertEquals("post 수정 완료", response.getMessage());
//
//    }
//
//    @Test
//    public void putNotWriterPutPosts() throws UnauthorizedAccessException {
//        //글 작성자가 아닌 경우 글 변경 불가능
//        //그룹에 새로운 멤버 추가
//        MemberRequestDto.save memberRequestDto = new MemberRequestDto.save();
//        memberRequestDto.setName("NoWriter");
//        memberRequestDto.setEmail("test@test.com");
//        memberRequestDto.setPassword("123");
//        Member member1 = new Member(memberRequestDto);
//
//        memberRepository.save(member1);
//
//        Long postId = this.post.getId();
//        Long memberId = member1.getId();
//        Long groupId = this.group.getId();
//
//        GroupMember groupMember1 = new GroupMember(member1, group, GroupRole.MEMBER);
//        groupMemberRepository.save(groupMember1);
//
//        //given
//        PostRequestDto.put requestDto = new PostRequestDto.put();
//        requestDto.setTitle("test");
//        requestDto.setContent("test");
//
//        //when & then
//        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
//            postService.putPost(postId, memberId, groupId, requestDto);
//        });
//    }
//
//    @Test
//    public void deletePost() throws UnauthorizedAccessException {
//        //given
//        Long postId = this.post.getId();
//        Long memberId = this.member.getId();
//
//        //when
//        postService.deletePost(postId, memberId);
//
//        //then
//        Assertions.assertThrows(Exception.class, () -> {
//            postRepository.findById(postId);
//        });
//    }
}
