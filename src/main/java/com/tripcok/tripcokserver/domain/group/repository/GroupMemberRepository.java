package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // 그룹 ID와 멤버 ID로 특정 GroupMember 조회
    Optional<GroupMember> findByGroup_IdAndMember_Id(Long groupId, Long memberId);

    // 멤버 ID와 그룹 ID로 특정 GroupMember 조회
    Optional<GroupMember> findGroupMemberByMember_IdAndAndGroup_Id(Long memberId, Long groupId);

    // 그룹의 관리자 멤버 조회
    @Query("select gm from GroupMember gm where gm.role = :adminRole and gm.member.id = :groupAdminId and gm.group.id = :groupId")
    Optional<GroupMember> findByGroupInAdminMember(
            @Param("groupId") Long groupId,
            @Param("adminRole") GroupRole role,
            @Param("groupAdminId") Long groupAdminId
    );

    /* 모임 조회 - 복수 <카테고리 적용> */
    @Query("select gm from GroupMember gm " +
            "join gm.group.category gc " +
            "join gc.category c " +
            "where c.id in :categoryIds")
    List<Group> findAllByCategoryIds(List<Long> categoryIds, Pageable pageable);


    // 사용자가 가입한 모든 그룹 조회
    @Query("select gm.group from GroupMember gm where gm.member.id = :memberId order by gm.createAt desc ")
    Page<Group> findGroupsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    // 사용자가 가입한 그룹 중 특정 카테고리를 가진 그룹 조회
    @Query("select gm.group from GroupMember gm " +
            "join gm.group.category gc " +
            "join gc.category c " +
            "where gm.member.id = :memberId and c.id in :categoryIds " +
            "order by gm.createAt desc "
            )
    Page<Group> findGroupsByMemberIdAndCategoryIds(
            @Param("memberId") Long memberId,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );

}
