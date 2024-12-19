package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import lombok.Data;

@Data
public class GroupCategoryResponse {

    private Long id;
    private String name;
    private Integer depth;

    public GroupCategoryResponse(PlaceCategory placeCategory) {
        this.id = placeCategory.getId();
        this.name = placeCategory.getName();
        this.depth = placeCategory.getDepth();
    }
}
