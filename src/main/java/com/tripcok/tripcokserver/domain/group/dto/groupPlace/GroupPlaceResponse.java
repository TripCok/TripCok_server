package com.tripcok.tripcokserver.domain.group.dto.groupPlace;

import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryResponse;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class GroupPlaceResponse {

    private Long placeId;
    private String placeThumbnail;
    private String placeName;
    private String placeDescription;
    private String placeAddress;
    private LocalTime placeStartTime;
    private LocalTime placeEndTime;
    private List<PlaceCategoryResponse> placeCategories;

    public GroupPlaceResponse(Place place) {
        this.placeId = place.getId();
        this.placeThumbnail = place.getImages().get(0).getImagePath();
        this.placeName = place.getName();
        this.placeDescription = place.getDescription();
        this.placeAddress = place.getAddress();
        this.placeStartTime = place.getStartTime();
        this.placeEndTime = place.getEndTime();
        this.placeCategories = place.getCategoryMappings().stream()
                .map(mapping -> new PlaceCategoryResponse(mapping.getCategory()))
                .toList();
    }
}
