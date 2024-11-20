package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Optional<GroupMember> findByGroup_IdAndMember_Id(Long groupId, Long memberId);


    @Query("select gm from GroupMember gm where gm.role = :adminRole and gm.member.id = :groupAdminId and gm.group.id =:groupId")
    Optional<GroupMember> findByGroupInAdminMember(
            @Param("groupId") Long groupId,
            @Param("adminRole") GroupRole role,
            @Param("groupAdminId") Long groupAdminId
    );
}
