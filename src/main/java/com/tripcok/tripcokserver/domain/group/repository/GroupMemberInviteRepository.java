package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.GroupMemberInvite;
import com.tripcok.tripcokserver.domain.group.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberInviteRepository extends JpaRepository<GroupMemberInvite, Long> {
    Optional<GroupMemberInvite> findByMember_IdAndGroup_IdAndInvitationStatus(Long memberId, Long groupId, InvitationStatus status);
}

