package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 장소 이름 */
    private String name;

    /* 장소 설명 */
    private String description;

    /* 장소 주소 */
    private String address;

    /* 운영 시간 */
    private LocalTime startTime;
    private LocalTime endTime;

    /* 장소 사진 */
    private String imagePath;

    /* 카테고리 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PlaceCategory category;

    /* 리뷰 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceReview> reviews;

    /* 구독 */
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceSubscribe> subscribes;
}