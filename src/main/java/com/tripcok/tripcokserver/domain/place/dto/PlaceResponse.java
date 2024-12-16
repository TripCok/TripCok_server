package com.tripcok.tripcokserver.domain.place.dto;

import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import com.tripcok.tripcokserver.domain.place.entity.PlaceImage;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class PlaceResponse {
    private Long id;
    private String name;
    private String description;
    private String placeThumbnail;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<PlaceCategory> categories;  // 부모-자식 포함한 카테고리 리스트
    private List<PlaceImageResponse> images; // 이미지 리스트

    public PlaceResponse(Place place, List<PlaceCategoryMapping> mappings) {
        this.id = place.getId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.placeThumbnail = getThumbnail(place.getImages());
        this.address = place.getAddress();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.startTime = place.getStartTime();
        this.endTime = place.getEndTime();
        this.createTime = place.getCreateTime();
        this.updateTime = place.getUpdateTime();

        // 부모 포함한 카테고리 리스트 구성
        this.categories = mappings.stream()
                .map(mapping -> buildCategoryHierarchy(mapping.getCategory()))
                .toList(); // 부모 포함한 카테고리 리스트 변환

        this.images = place.getImages().stream()
                .map(PlaceImageResponse::new)
                .toList(); // 이미지 변환
    }

    private String getThumbnail(List<PlaceImage> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.get(0).getImagePath();
    }

    /**
     * 부모 포함한 카테고리 계층 구조를 구성
     */
    private PlaceCategory buildCategoryHierarchy(PlaceCategory category) {
        PlaceCategory current = category;

        // 부모-자식 계층을 리스트로 구성
        while (current.getParentCategory() != null) {
            current = current.getParentCategory();
        }

        return category; // 최상위 부모를 반환
    }
}



