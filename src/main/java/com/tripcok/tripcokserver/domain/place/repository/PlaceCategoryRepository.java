package com.tripcok.tripcokserver.domain.place.repository;

import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {

    // 부모가 없는 최상위 카테고리 조회
    List<PlaceCategory> findAllByParentCategoryIsNull();

    // 특정 부모 아래에서 이름 중복 여부 확인
    boolean existsByNameAndParentCategoryId(String placeName, Long id);

    List<PlaceCategory> findByDepthAndName(Integer depth, String name);

    List<PlaceCategory> findByParentCategoryIdAndNameAndDepth(Long parentCategoryId, String name, Integer depth);
}
