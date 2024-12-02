package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // 모든 그룹 조회 (페이징)
    Page<Group> findAllByOrderByCreateAtDesc(Pageable pageable);

    // 그룹 이름으로 검색
    @Query("select g from Group g where lower(g.groupName) like lower(concat('%', :query, '%')) order by g.createAt desc")
    Page<Group> findByGroupNameContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    // 카테고리에 따라 그룹 조회 (페이징)
    @Query("select g from Group g " +
            "join g.category gc " +
            "where gc.category.id in :categoryIds " +
            "order by g.createAt desc ")
    Page<Group> findAllByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);
}

