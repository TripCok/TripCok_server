package com.tripcok.tripcokserver.domain.place.repository;

import com.tripcok.tripcokserver.domain.place.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT DISTINCT p FROM Place p JOIN p.categoryMappings cm WHERE cm.category.id IN :categoryIds")
    Page<Place> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    // 카테고리 ID와 이름으로 필터링
    @Query("select p from Place p " +
            "join p.categoryMappings cm " +
            "join cm.category c " +
            "where p.name like concat('%', :name, '%') " +
            "and c.id in :categoryIds " +
            "order by p.createAt desc ")
    Page<Place> findByCategoryIdsAndNameContaining(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("name") String name,
            Pageable pageable
    );

    // 이름으로만 필터링
    Page<Place> findByNameContaining(String name, Pageable pageable);
}
