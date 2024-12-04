package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.GroupPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupPlaceRepository extends JpaRepository<GroupPlace, Long> {

    List<GroupPlace> findByGroup_IdOrderByOrdersDesc(Long groupId);

    Page<GroupPlace> findByGroup_IdOrderByOrdersAsc(Long groupId, Pageable pageable);

    List<GroupPlace> findByGroupIdOrderByOrdersAsc(Long groupId);
}
