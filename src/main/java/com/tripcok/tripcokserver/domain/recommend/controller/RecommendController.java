package com.tripcok.tripcokserver.domain.recommend.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.recommend.dto.RecommendRequestDto;
import com.tripcok.tripcokserver.domain.recommend.dto.RecommendResponseDto;
import com.tripcok.tripcokserver.domain.recommend.entity.Recommend;
import com.tripcok.tripcokserver.domain.recommend.service.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PutMapping("/api/v1/Recommend")
    @Operation(summary = "추천 결과 저장", description = "추천 결과 저장.")
    public ResponseEntity<RecommendResponseDto> setRecommend(@RequestBody RecommendRequestDto requestDto) {
        RecommendResponseDto responseDto = recommendService.putRecommend(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    
    @GetMapping("/api/v1/getRecommends")
    @Operation(summary = "추천 결과 리스트", description = "추천 결과 리스트.")
    public ResponseEntity<?> getRecommends() {
        Page<PlaceResponse> recommendList = recommendService.getRecommendPlaces();
        return ResponseEntity.status(HttpStatus.OK).body(recommendList);
    }
}
