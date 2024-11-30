package com.tripcok.tripcokserver.domain.place.dto;

import lombok.Data;

@Data
public class PlaceCategoryRequest {

    private Long memberId; // 요청자 아이디
    private String placeName; // 여행지 이름
    private Long parentId; // 카테고리 부모 ID
}
