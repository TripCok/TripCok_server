package com.tripcok.tripcokserver.domain.place.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryRequest;
import com.tripcok.tripcokserver.domain.place.service.PlaceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/place/category")
@RequiredArgsConstructor
public class PlaceCategoryController {

    private final PlaceCategoryService pcs;

    /* 여행지 카테고리 생성 */
    @PostMapping
    public ResponseEntity<?> createPlaceCategory(PlaceCategoryRequest request) throws AccessDeniedException {
        return pcs.createCategory(request);
    }

    /* 여행지 카테고리 모두 조회 */
    @GetMapping("/all")
    public ResponseEntity<?> getPlaceCategory() {
        return pcs.findByAllCategory();
    }


}
