package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryMappingRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceCategoryMappingRepository placeCategoryMappingRepository;

    @Value("${save.location.place-thumbnail}")
    private String savePath;

    @Transactional
    public ResponseEntity<?> save(PlaceRequest.save request) {

        if (validatePlaceRequest(request)) {

        }

        try {
            // 1. Place 엔티티 생성
            Place place = Place.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .address(request.getAddress())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .build();

            Place savedPlace = placeRepository.save(place);

            // 2. 카테고리 매핑 저장
            if (request.getCategoryIds() != null) {
                for (Long categoryId : request.getCategoryIds()) {
                    PlaceCategory category = placeCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID: " + categoryId));

                    PlaceCategoryMapping mapping = new PlaceCategoryMapping(savedPlace, category);

                    placeCategoryMappingRepository.save(mapping);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ResponseEntity.ok(savedPlace);
    }

    /**/
    private boolean validatePlaceRequest(PlaceRequest.save request) {
        if (request.getName() || request.getName()) {
            return false;
        }
        return true;
    }
}
