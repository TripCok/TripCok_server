package com.tripcok.tripcokserver.domain.place.dto;

import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlaceCategoryResponse {

    private Long id;
    private String name;
    private List<PlaceCategoryResponse> children; // 자식 카테고리 리스트
    private Integer depth;

    public PlaceCategoryResponse(PlaceCategory placeCategory) {
        this.id = placeCategory.getId();
        this.name = placeCategory.getName();
        this.children = placeCategory.getChildCategories().stream()
                .map(PlaceCategoryResponse::new)
                .toList();
        this.depth = placeCategory.getDepth();
    }
}
