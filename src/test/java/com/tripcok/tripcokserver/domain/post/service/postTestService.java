package com.tripcok.tripcokserver.domain.post.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.board.BoardRepository;
import com.tripcok.tripcokserver.domain.post.dto.PostResponseDto;
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

import com.tripcok.tripcokserver.domain.post.entity.Type;
import com.tripcok.tripcokserver.domain.post.repository.PostRepository;
import com.tripcok.tripcokserver.domain.postcomment.dto.PostCommentResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.service.PostCommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class postTestService {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private PostRequestDto.create postRequestDto;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;

    private Group group;

    private Post post;

    private Board board;
    @Autowired
    private PostCommentService postCommentService;

    @BeforeEach
    void setUp() {

        // 테스트 데이터 준비
        MemberRequestDto.save memberRequestDto = new MemberRequestDto.save();
        memberRequestDto.setEmail("test@example.com");
        memberRequestDto.setPassword("testpassword");
        memberRequestDto.setName("test");

        // 사용자 등록
        this.member = memberRepository.save(new Member(memberRequestDto));

        GroupRequestDto groupRequestDto = new GroupRequestDto();
        groupRequestDto.setGroupName("Test Group");
        groupRequestDto.setCategory("Category");
        groupRequestDto.setDescription("Test Group Description");

        this.board = new Board();

        // 그룹 등록
        this.group = groupRepository.save(new Group(groupRequestDto, board));

        this.group.setId(member.getId());

        postRequestDto = new PostRequestDto.create();
        postRequestDto.setTitle("Test Post");
        postRequestDto.setContent("Test Content");
        postRequestDto.setType(Type.COMMON);

        groupMemberRepository.save(new GroupMember(this.member, this.group, GroupRole.ADMIN));

        // Board 객체 생성 및 그룹 추가
        this.board = new Board();
        this.board.addGroup(this.group);

        // Board 등록
        this.board = boardRepository.save(board);

        // 그룹에 Board 설정
        this.group.setBoard(this.board);
    }

    @Test
    @Rollback(value = false)
    void getDataGroup(){

        // 사용자
        System.out.println(member.getName() + " : " + member.getId());
        // 그룹
        System.out.println(group.getGroupName() + " : " + group.getId());

        System.out.println(board.getId() + ", Board Group Id : " + board.getGroup().getId());

        // 이미 생성된 그룹과 생성된 보드가 속해 있는 그룹의 아이디는 같아야 한다.
        Assertions.assertEquals(group.getId(), board.getGroup().getId());

        PostResponseDto.create responseOne = postService.createPost(member.getId(), group.getId(), postRequestDto);

        System.out.println(responseOne.getPostId() + " : " + responseOne.getType().toString());

        postRequestDto.setType(Type.NOTICE);

        PostResponseDto.create responseTwo = postService.createPost(member.getId(), group.getId(), postRequestDto);

        System.out.println(responseTwo.getPostId() + " : " + responseTwo.getType().toString());

    }

    @Test
    void testCreatePost() {

        // when
        PostResponseDto.create response = postService.createPost(member.getId(), group.getId(), postRequestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("게시글 추가 완료");

        this.post = postRepository.findById(response.getPostId()).orElse(null);

        assertThat(this.post).isNotNull();
        assertThat(this.post.getTitle()).isEqualTo(postRequestDto.getTitle());
        assertThat(this.post.getContent()).isEqualTo(postRequestDto.getContent());
    }

    @Test
    void testCreateNotice() {

        postRequestDto.setType(Type.NOTICE);
        // when
        PostResponseDto.create response = postService.createNotice(member.getId(), group.getId(), postRequestDto);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("공지사항 추가 완료");
    }

}
