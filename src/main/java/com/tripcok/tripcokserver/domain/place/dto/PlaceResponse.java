package com.tripcok.tripcokserver.domain.place.dto;

import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import com.tripcok.tripcokserver.domain.place.entity.PlaceImage;
import lombok.Data;

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
    private List<String> categories;
    private List<PlaceImageResponse> images;

    public PlaceResponse(Place place, List<PlaceCategoryMapping> mappings) {
        this.id = place.getId();
        this.name = place.getName();
        this.description = place.getDescription();
        this.address = place.getAddress();
        this.startTime = place.getStartTime();
        this.endTime = place.getEndTime();
        this.categories = mappings.stream()
                .map(mapping -> mapping.getCategory().getName())
                .toList();
        this.images = place.getImages().stream()
                .map(PlaceImageResponse::new)
                .toList();
    }
}

