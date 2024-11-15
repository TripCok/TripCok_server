package com.tripcok.tripcokserver.domain.place.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Place {
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
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /* 장소 사진 */
    private String imagePath;

    /* 리뷰 */
    private String review;

    /* 찜 */
    private String subscribe;
}
