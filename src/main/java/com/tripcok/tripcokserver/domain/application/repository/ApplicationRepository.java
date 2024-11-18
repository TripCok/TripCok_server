package com.tripcok.tripcokserver.domain.application.repository;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository  extends JpaRepository<Group, Long> {
}
