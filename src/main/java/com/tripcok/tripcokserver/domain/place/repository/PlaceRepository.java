package com.tripcok.tripcokserver.domain.place.repository;

import com.tripcok.tripcokserver.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
