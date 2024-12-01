package com.tripcok.tripcokserver.domain.recommend.dto;

import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import lombok.Data;

@Data
public class RecommendResponseDto {

    private Long score;
    private Long group_id;

    //place 정보
    private PlaceResponse place;


    public RecommendResponseDto(Long score, Long group_id, PlaceResponse place) {
        this.score = score;
        this.group_id = group_id;
        this.place = place;
    }
}
