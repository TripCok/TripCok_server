package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.GroupMemberInvite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberInviteRepository extends JpaRepository<GroupMemberInvite, Long> {
}
