package com.tripcok.tripcokserver.domain.place.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryRequest;
import com.tripcok.tripcokserver.domain.place.service.PlaceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/place/category")
@Tag(name = "PlaceCategory API", description = "여행 카테고리 API")
@RequiredArgsConstructor
public class PlaceCategoryController {

    private final PlaceCategoryService pcs;

    /* 여행지 카테고리 생성 */
    @PostMapping
    @Operation(summary = "여행지 카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "카테고리가 생성 되었습니다.")
    public ResponseEntity<?> createPlaceCategory(@RequestBody PlaceCategoryRequest request) throws AccessDeniedException {
        return pcs.createCategory(request);
    }

    /* 여행지 카테고리 모두 조회 */
    @GetMapping("/all")
    @Operation(summary = "여행지 카테고리 모두 조회", description = "모든 카테고리를 조회 합니다.")
    @ApiResponse(responseCode = "200", description = "모든 카테고리가 조회 되었습니다.")
    public ResponseEntity<?> getPlaceCategory() {
        return pcs.findByAllCategory();
    }


}
