package com.tripcok.tripcokserver.domain.place.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    /* 여행지 생성 */
    @PostMapping
    public ResponseEntity<?> createPlace(@RequestBody PlaceRequest.save request) throws AccessDeniedException {
        return placeService.savePlace(request);
    }

    /* 여행지 상세 조회 */
    @GetMapping("/{placeId}")
    public ResponseEntity<?> getPlaceDetails(@PathVariable Long placeId) {
        return placeService.getPlaceDetails(placeId);
    }

    /* 모든 여행지 조회 - 카테고리 별로 필터 가능하게 */
    @GetMapping
    public ResponseEntity<?> getAllPlaces(
            @RequestParam(required = false) List<Long> categoryId,
            Pageable pageable) {
        Page<PlaceResponse> responsePage = placeService.getAllPlaces(categoryId, pageable);
        return ResponseEntity.ok(responsePage);
    }

    /* 여행지 삭제*/
    @DeleteMapping("/{placeId}")
    public ResponseEntity<?> deletePlace(@PathVariable Long placeId, @RequestParam Long memberId) throws AccessDeniedException {
        return placeService.deletePlace(placeId, memberId);
    }
}
