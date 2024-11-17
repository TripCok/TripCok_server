package com.tripcok.tripcokserver.domain.place.repository;


import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PlaceCategoryMappingRepository extends JpaRepository<PlaceCategoryMapping, Long> {

}
