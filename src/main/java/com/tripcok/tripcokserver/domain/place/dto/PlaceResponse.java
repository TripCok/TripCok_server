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
    private List<String> categoryNames;
    private List<PlaceImageResponse> images;

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

        // 카테고리 이름만 추출
        this.categoryNames = mappings.stream()
                .map(mapping -> mapping.getCategory().getName())
                .toList();

        this.images = place.getImages().stream()
                .map(PlaceImageResponse::new)
                .toList();
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
        // 필요한 깊이까지만 카테고리 계층 구성
        PlaceCategory current = category;
        PlaceCategory topLevelCategory = null;

        // 최상위 부모만 반환
        while (current.getParentCategory() != null) {
            topLevelCategory = current.getParentCategory();
            current = topLevelCategory;
        }

        return topLevelCategory;
    }

}






