package com.tripcok.tripcokserver.domain.recommend.dto;

import lombok.Data;

@Data
public class RecommendRequestDto {
    private Long group_id;
    private Long place_id;
    private Long score;
}
