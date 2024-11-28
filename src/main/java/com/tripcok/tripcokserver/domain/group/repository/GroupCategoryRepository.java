package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.GroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long> {

    Optional<GroupCategory> findByGroupIdAndCategoryId(Long groupId, Long categoryId);

    void deleteByGroupIdAndCategoryId(Long groupId, Long categoryId);
}
