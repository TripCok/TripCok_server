package com.tripcok.tripcokserver.domain.application.repository;

import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.application.entity.ApplicationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationMemberRepository extends JpaRepository<ApplicationMember, Long> {
    Optional<ApplicationMember> findByApplicationAndMember(Application application, ApplicationMember member);
}
