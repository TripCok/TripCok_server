package com.tripcok.tripcokserver.domain.place.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
@Tag(name = "Place API", description = "여행지 API")
public class PlaceController {

    private final PlaceService placeService;

    /* 여행지 생성 */
    @PostMapping
    @Operation(summary = "여행지 생성", description = "새로운 여행지를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "여행지가 생성되었습니다.")
    public ResponseEntity<?> createPlace(
            @RequestPart("request") @Valid PlaceRequest.placeSave request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws AccessDeniedException {
        request.convertToLocalTime();

        return placeService.savePlace(request, files);
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

    /* 여행지 수정 */
    @PutMapping("/{placeId}")
    @Operation(summary = "여행지 수정", description = "기존 여행지 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "여행지 정보가 성공적으로 수정되었습니다.")
    public ResponseEntity<?> updatePlace(
            @PathVariable Long placeId,
            @RequestPart("request") @Valid PlaceRequest.placeUpdate request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws AccessDeniedException {
        return placeService.updatePlace(placeId, request, files);
    }

    /* 여행지 삭제*/
    @DeleteMapping("/{placeId}")
    @Operation(summary = "여행지 삭제", description = "여행지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "해당 여행지를 삭제합니다.")
    public ResponseEntity<?> deletePlace(@PathVariable Long placeId, @RequestParam Long memberId) throws AccessDeniedException {
        return placeService.deletePlace(placeId, memberId);
    }
}
