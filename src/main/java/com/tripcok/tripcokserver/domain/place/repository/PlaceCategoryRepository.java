package com.tripcok.tripcokserver.domain.place.repository;

import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
}
