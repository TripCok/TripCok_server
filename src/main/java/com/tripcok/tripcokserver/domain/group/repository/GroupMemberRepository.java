package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    // 특정 member와 group의 관계를 조회하는 메서드 예시
    List<GroupMember> findByGroupId(Long groupId);
}
