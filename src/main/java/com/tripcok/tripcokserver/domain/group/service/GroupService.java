package com.tripcok.tripcokserver.domain.group.service;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.dto.*;
import com.tripcok.tripcokserver.domain.group.entity.*;
import com.tripcok.tripcokserver.domain.group.repository.GroupCategoryRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberInviteRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tripcok.tripcokserver.domain.group.entity.GroupRole.ADMIN;

@Service
@AllArgsConstructor
@Transactional
public class GroupService {


    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberInviteRepository groupMemberInviteRepository;
    private final GroupCategoryRepository groupCategoryRepository;
    private final PlaceCategoryRepository categoryRepository;

    /**
     * - 모임 생성
     * 1. 모임을 생성
     * 2. 모임 리스트에 사용자와, 모임 추가
     */
    public GroupResponseDto createGroup(@Valid GroupRequestDto requestDto) {

        /* #1 그룹 생성 */
        Board board = new Board();

        Group group = new Group(requestDto, board);

        board.addGroup(group);

        Group newGroup = groupRepository.save(group);

        /* 카테고리 생성 등록 */
        List<GroupCategory> newGroupList = requestDto.getCategories().stream()
                .map(categoryId -> {
                    PlaceCategory placeCategory = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NoSuchElementException("category not found: " + categoryId));
                    return new GroupCategory(newGroup, placeCategory);

                }).toList();


        Member findMember = memberRepository.findById(requestDto.getMemberId()).orElseThrow(
                () -> new EntityNotFoundException("찾을수 없는 사용자 입니다.")
        );

        /* #2 그룹을 생성한 사람을 GroupMember에 추가 */
        GroupMember groupMember = new GroupMember(
                findMember,
                newGroup,
                ADMIN
        );

        GroupMember saveGroupMember = groupMemberRepository.save(groupMember);

        List<GroupCategory> groupCategoryList = groupCategoryRepository.saveAll(newGroupList);

        List<GroupMember> groupMembers = new ArrayList<>();
        groupMembers.add(saveGroupMember);

        return new GroupResponseDto(newGroup, groupCategoryList, groupMembers);
    }

    // 2. 모임 조회 - 단일
    public GroupResponseDto getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));

        return new GroupResponseDto(group);
    }

    // 3. 모임 조회 - 복수 (Pageable)
    public Page<?> getGroups(List<Long> categoryIds, String query, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Group> groupsPage;

        if (categoryIds != null && !categoryIds.isEmpty()) {
            // 1. 카테고리가 있는 경우
            groupsPage = groupRepository.findAllByCategoryIds(categoryIds, pageable);
        } else {
            // 2. 카테고리가 없는 경우
            groupsPage = groupRepository.findAllByOrderByCreateAtDesc(pageable);
        }

        if (query != null && !query.isEmpty()) {
            // 해당 그룹이름이 있는 경우
            groupsPage = groupRepository.findByGroupNameContainingIgnoreCase(query, pageable);
        } else {
            // 해당 그룹이름이 없는 경우
            groupsPage = groupRepository.findAllByOrderByCreateAtDesc(pageable);
        }

        // Page<Group> -> Page<GroupAllResponseDto> 변환
        return groupsPage.map(GroupAllResponseDto::new);
    }

    /* 내가 가입된 모임 조회 */
    public ResponseEntity<?> getMyGroups(List<Long> categoryIds, Integer pageNum, Integer pageSize, Long memberId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Group> groupsPage;

        if (categoryIds != null && !categoryIds.isEmpty()) {
            // 1. 카테고리가 있는 경우: 사용자가 가입한 그룹 중 카테고리가 일치하는 그룹 조회
            groupsPage = groupMemberRepository.findGroupsByMemberIdAndCategoryIds(memberId, categoryIds, pageable);
        } else {
            // 2. 카테고리가 없는 경우: 사용자가 가입한 모든 그룹 조회
            groupsPage = groupMemberRepository.findGroupsByMemberId(memberId, pageable);
        }

        // Page<Group> -> Page<GroupResponseDto> 변환
        Page<GroupResponseDto> responsePage = groupsPage.map(GroupResponseDto::new);

        return ResponseEntity.ok(responsePage);
    }

    // 4. 모임 수정
    public GroupResponseDto updateGroup(Long id, @Valid GroupRequestDto.update requestDto) {
        // ID로 그룹 조회 (없으면 예외 발생)
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));

        // 그룹 정보 업데이트
        group.setGroupName(requestDto.getGroupName());
        group.setDescription(requestDto.getDescription());
//        group.setCategory(requestDto.getCategory());

        // 업데이트된 그룹 저장!
        Group saveGroup = groupRepository.save(group);

        // 업데이트된 데이터를 DTO로 반환
        return new GroupResponseDto(saveGroup);
    }

    // 4. 모임 구인상태 변경
    public void updateRecruitingStatus(Long groupId, Long memberId, Boolean recruiting) {
        // 그룹 조회
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + groupId));
        // 요청한 사용자가 해당 그룹의 ADMIN 인지 권환 확인
        GroupMember adminGroupMember = groupMemberRepository.findByGroupInAdminMember(groupId, ADMIN, memberId)
                .orElseThrow(() -> new EntityNotFoundException("상태 변경 권한이 없습니다!"));
        // 모임 상태 변경
        group.setRecruiting(recruiting);

        // 변경된 상태 저장
        groupRepository.save(group);
    }

    // 5. 모임 삭제
    public void deleteGroup(Long id) {
        /* 그룹 조회 */
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID로 그룹을 찾을 수 없습니다!: " + id));

        /* 그룹 삭제 */
        groupRepository.delete(group);
    }

    // 10. 모임 초대
    public ResponseEntity<?> inviteMember(GroupInviteDto groupInviteDto) {

        /* 초대한 사람의 역할 확인*/
        Optional<Member> byId = memberRepository.findById(groupInviteDto.getGroupAdminId());

        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Member sender = byId.get();

        /* 초대 받는 사람의 존재 여부 체크 */
        Optional<Member> receiver = memberRepository.findByEmail(groupInviteDto.getEmail());

        if (receiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("초대하려는 사용자를 찾을 수 없습니다.");
        }

        /* 중복 초대 방지 */
        Optional<GroupMemberInvite> byMemberIdAndGroupId = groupMemberInviteRepository.findByMember_IdAndGroup_Id(receiver.get().getId(), groupInviteDto.getGroupId());
        if (byMemberIdAndGroupId.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 초대된 사용자 입니다.");
        }

        /* 이미 모임에 가입되어 있는 멤버의 경우 초대가 가지 않음 */
        Optional<GroupMember> receiverInGroup = groupMemberRepository.findByGroup_IdAndMember_Id(groupInviteDto.getGroupId(), receiver.get().getId());

        if (receiverInGroup.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 모임에 가입되어있는 사용자입니다.");
        }

        /* Receiver의 모임에서의 권한 체크 */
        GroupMember senderInGroup = groupMemberRepository.findByGroup_IdAndMember_Id(groupInviteDto.getGroupId(), sender.getId()).orElseThrow(
                () -> new NotFoundException("옳바르지 않은 요청입니다.")
        );

        if (!senderInGroup.getRole().equals(ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("사용자를 초대할 수 있는 권환이 없습니다.");
        }

        Group group = groupRepository.findById(groupInviteDto.getGroupId()).orElseThrow(
                () -> new EntityNotFoundException("옳바르지 않은 요청입니다.")
        );

        /* 초대 받는 사람 검색 */
        GroupMemberInvite groupMemberInvite = new GroupMemberInvite(receiver.get(), group);
        groupMemberInviteRepository.save(groupMemberInvite);

        return ResponseEntity.status(HttpStatus.OK).body("모임 초대에 성공하였습니다.");
    }

    // 11. 모임 초대 수락
    public ResponseEntity<?> acceptInvite(InviteRequestDto request) {
        GroupMemberInvite invite = groupMemberInviteRepository.findById(request.getInviteId())
                .orElseThrow(() -> new EntityNotFoundException("해당 초대를 찾을 수 없습니다."));

        if (!invite.getMember().getId().equals(request.getMemberId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 초대가 초대한 멤버와 일치하지 않습니다");
        }
        /* 멤버 추가 및 초대 삭제 처리 */
        handleInviteAcceptance(invite);
        return ResponseEntity.status(HttpStatus.OK).body("모임 초대가 성공적으로 수락되었습니다.");

    }

    private void handleInviteAcceptance(GroupMemberInvite invite) {
        Group group = invite.getGroup();
        Member member = invite.getMember();

        // 2. 모임 멤버로 추가
        GroupMember newGroupMember = new GroupMember(member, group, GroupRole.MEMBER);
        groupMemberRepository.save(newGroupMember);

        // 3. 초대 행 삭제
        groupMemberInviteRepository.delete(invite);
    }

    /* 그룹에 카테고리 추가 */
    @Transactional(rollbackFor = {NotFoundException.class, Exception.class})
    public ResponseEntity<?> addGroupCategories(Long id, List<Long> categories) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NoSuchElementException("옳 바르지 않은 요청입니다."));

        List<GroupCategory> savingCategory = categories.stream().map(categoryId -> {
            /* 이미 중복되는 카테고리는 저장 안되게 */
            Optional<GroupCategory> byGroupIdAndCategoryId = groupCategoryRepository.findByGroupIdAndCategoryId(group.getId(), categoryId);
            if (byGroupIdAndCategoryId.isEmpty()) {
                PlaceCategory placeCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException(""));
                return new GroupCategory(group, placeCategory);
            }
            return null;
        }).toList();

        try {

            groupCategoryRepository.saveAll(savingCategory);
        } catch (Exception e) {
            new Exception(e.getCause());
        }

        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 추가하였습니다.");


    }

    /* 카테고리 삭제 */
    public ResponseEntity<?> deleteGroupCategory(Long id, Long categoryId) {
        try {
            groupCategoryRepository.deleteByGroupIdAndCategoryId(id, categoryId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제 되었습니다.");

    }

}
