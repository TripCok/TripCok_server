package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    private String address;

    private LocalTime startTime;

    private LocalTime endTime;

    private Double latitude;

    private Double longitude;

    private Integer mlMappingId;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<PlaceImage> images = new ArrayList<>();

    /* 리뷰 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<PlaceReview> reviews = new ArrayList<>();

    /* 구독 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)

    private final List<PlaceSubscribe> subscribes = new ArrayList<>();

    /* 카테고리 매핑 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<PlaceCategoryMapping> categoryMappings = new ArrayList<>();

    public Place(PlaceRequest.placeSave request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.address = request.getAddress();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
    }

    public void addImage(PlaceImage image) {
        images.add(image);
        image.setPlace(this);
    }

    public void removeImage(PlaceImage image) {
        images.remove(image);
        image.setPlace(null);
    }

    public void update(PlaceRequest.placeUpdate request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getAddress() != null) {
            this.address = request.getAddress();
        }
        if (request.getStartTime() != null) {
            this.startTime = request.getStartTime();
        }
        if (request.getEndTime() != null) {
            this.endTime = request.getEndTime();
        }

    }
}