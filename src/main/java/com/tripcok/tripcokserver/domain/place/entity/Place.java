package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
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

    private String imagePath;

    /* 리뷰 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceReview> reviews;

    /* 구독 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceSubscribe> subscribes;

    /* 카테고리 매핑 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceCategoryMapping> categoryMappings;

    public Place(PlaceRequest.save request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.address = request.getAddress();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.imagePath = ""; /* 임시 저장 목적 */
    }
}