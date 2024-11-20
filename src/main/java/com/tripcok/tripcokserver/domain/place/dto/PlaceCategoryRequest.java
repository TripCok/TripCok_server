package com.tripcok.tripcokserver.domain.place.dto;

import lombok.Data;

@Data
public class PlaceCategoryRequest {

    /* 요청자 아이디 */
    private Long memberId;

    private String name;

    private Long parentId;
}
