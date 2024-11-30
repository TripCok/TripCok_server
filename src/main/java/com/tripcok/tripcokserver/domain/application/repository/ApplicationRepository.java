package com.tripcok.tripcokserver.domain.application.repository;

import com.tripcok.tripcokserver.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
