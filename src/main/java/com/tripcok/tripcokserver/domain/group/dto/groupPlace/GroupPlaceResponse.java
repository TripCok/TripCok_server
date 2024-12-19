package com.tripcok.tripcokserver.domain.group.dto.groupPlace;

import com.tripcok.tripcokserver.domain.group.entity.GroupPlace;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryResponse;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceImage;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class GroupPlaceResponse {

    private Long id;
    private Long placeId;
    private String placeThumbnail;
    private String placeName;
    private String placeDescription;
    private String placeAddress;
    private LocalTime placeStartTime;
    private LocalTime placeEndTime;
    private Double placeLatitude;
    private Double placeLongitude;
    private Integer orders;
    private List<PlaceCategoryResponse> placeCategories;

    public GroupPlaceResponse(GroupPlace groupPlace) {
        this.id = groupPlace.getId();
        this.placeId = groupPlace.getPlace().getId();
        this.placeThumbnail = getThumbnail(groupPlace.getPlace().getImages());
        this.placeName = groupPlace.getPlace().getName();
        this.placeDescription = groupPlace.getPlace().getDescription();
        this.placeAddress = groupPlace.getPlace().getAddress();
        this.placeStartTime = groupPlace.getPlace().getStartTime();
        this.placeEndTime = groupPlace.getPlace().getEndTime();
        this.placeLatitude = groupPlace.getPlace().getLatitude();
        this.placeLongitude = groupPlace.getPlace().getLongitude();
        this.orders = groupPlace.getOrders();
        this.placeCategories = groupPlace.getPlace().getCategoryMappings().stream()
                .map(mapping -> new PlaceCategoryResponse(mapping.getCategory()))
                .toList();
    }

    private String getThumbnail(List<PlaceImage> images) {
        String path = null;
        try {
            path = images.get(0).getImagePath();

        } catch (IndexOutOfBoundsException e) {
            path = null;
        }
        return path;
    }


}
