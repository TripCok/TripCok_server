package com.tripcok.tripcokserver.domain.application.repository;

import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByGroupIdOrderByCreateAtDesc(Long group_id, Pageable pageable);

    Optional<Application> findByGroupAndMember(Group group, Member member);

    @Query("SELECT a FROM Application a WHERE a.group.id = :groupId ORDER BY a.createAt DESC")
    Page<Application> findApplicationsByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}