package com.tripcok.tripcokserver.domain.post.entity;

import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
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

    /* 게시글 만들기 */
    public PostResponseDto createPost(Long userId, Long groupId, @Valid PostRequestDto requestDto) {

        // 사용자 조회
        Member member = findMemberById(userId);

        // 그룹 및 사용자 검증
        Group group = findGroupById(groupId);
        validateGroupMembership(groupId, member);

        // 그룹의 Board 가져오기
        Board board = group.getBoard();
        log.info("Board ID: {}", board.getId());

        // 게시글 생성 및 저장
        Post post = createAndSavePost(requestDto, board);

        return new PostResponseDto("게시글 추가 완료", post.getId());
    }

    /* 공지 사항 */

    //사용자 조회
    //사용자 검증 - (권한이 ADMIN인가)
    //그룹의 Board를 가지고 오기
    //게시글 생성 및 저장 - type을 Notice로 하기


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
}
