package com.tripcok.tripcokserver.domain.place.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Place API", description = "여행지 API")
public class PlaceController {

    private final PlaceService placeService;

    /* 여행지 생성 */
    @PostMapping
    @Operation(summary = "여행지 생성", description = "새로운 여행지를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "여행지가 생성 되었습니다.")
    public ResponseEntity<?> createPlace(@RequestBody PlaceRequest.save request) throws AccessDeniedException {
        return placeService.savePlace(request);
    }

    /* 여행지 상세 조회 */
    @GetMapping("/{placeId}")
    @Operation(summary = "여행지 상세 조회", description = "여행지를 상세 조회 합니다.")
    @ApiResponse(responseCode = "200", description = "여행지를 상세 조회 했습니다.")
    public ResponseEntity<?> getPlaceDetails(@PathVariable Long placeId) {
        return placeService.getPlaceDetails(placeId);
    }

    /* 모든 여행지 조회 - 카테고리 별로 필터 가능하게 */
    @GetMapping
    @Operation(summary = "모든 여행지 조회", description = "선택된 카테고리 별로 모든 여행지를 조회 합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 별로 필터된 여행지가 조회 되었습니다.")
    public ResponseEntity<?> getAllPlaces(
            @RequestParam(required = false) List<Long> categoryId,
            Pageable pageable) {
        Page<PlaceResponse> responsePage = placeService.getAllPlaces(categoryId, pageable);
        return ResponseEntity.ok(responsePage);
    }

    /* 여행지 삭제*/
    @DeleteMapping("/{placeId}")
    @Operation(summary = "여행지 삭제", description = "여행지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "해당 여행지를 삭제합니다.")
    public ResponseEntity<?> deletePlace(@PathVariable Long placeId, @RequestParam Long memberId) throws AccessDeniedException {
        return placeService.deletePlace(placeId, memberId);
    }
}
