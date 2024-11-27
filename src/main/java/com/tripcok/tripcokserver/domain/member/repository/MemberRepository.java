package com.tripcok.tripcokserver.domain.member.repository;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    List<Member> findByGroupMembersGroupId(Long groupId);

    Page<Member> findByNameContainingOrEmailContaining(String name, String email, Pageable pageable);


}