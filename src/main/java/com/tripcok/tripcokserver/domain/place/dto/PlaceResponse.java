package com.tripcok.tripcokserver.domain.place.dto;

import com.tripcok.tripcokserver.domain.place.entity.Place;
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
    private String address;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> categories;  // 카테고리 이름 리스트
    private List<PlaceImageResponse> images; // 이미지 리스트

    public PlaceResponse(Place place, List<PlaceCategoryMapping> mappings) {
        this.id = place.getId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.address = place.getAddress();
        this.startTime = place.getStartTime();
        this.endTime = place.getEndTime();
        this.createTime = place.getCreateTime();
        this.updateTime = place.getUpdateTime();
        this.categories = mappings.stream()
                .map(mapping -> mapping.getCategory().getName())
                .toList(); // 카테고리 이름 변환
        this.images = place.getImages().stream()
                .map(PlaceImageResponse::new)
                .toList(); // 이미지 변환
    }
}

