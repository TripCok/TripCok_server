package com.tripcok.tripcokserver.domain.group.dto.groupPlace;

import com.tripcok.tripcokserver.domain.group.entity.GroupPlace;
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

    public GroupPlaceResponse(GroupPlace groupPlace) {
        this.placeId = groupPlace.getPlace().getId();
        this.placeThumbnail = groupPlace.getPlace().getImages().get(0).getImagePath();
        this.placeName = groupPlace.getPlace().getName();
        this.placeDescription = groupPlace.getPlace().getDescription();
        this.placeAddress = groupPlace.getPlace().getAddress();
        this.placeStartTime = groupPlace.getPlace().getStartTime();
        this.placeEndTime = groupPlace.getPlace().getEndTime();
        this.placeCategories = groupPlace.getPlace().getCategoryMappings().stream()
                .map(mapping -> new PlaceCategoryResponse(mapping.getCategory()))
                .toList();
    }
}
