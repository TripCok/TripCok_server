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
    private String description;
    private String address;

    private LocalTime startTime;
    private LocalTime endTime;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceImage> images = new ArrayList<>();

    /* 리뷰 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlaceReview> reviews = new ArrayList<>();

    /* 구독 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlaceSubscribe> subscribes = new ArrayList<>();

    /* 카테고리 매핑 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceCategoryMapping> categoryMappings = new ArrayList<>();

    public Place(PlaceRequest.save request) {
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
}