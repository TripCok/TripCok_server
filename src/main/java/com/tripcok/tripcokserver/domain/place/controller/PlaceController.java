package com.tripcok.tripcokserver.domain.place.controller;

import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    /* 여행지 생성 */
    @PostMapping
    public ResponseEntity<?> createPlace(@RequestBody PlaceRequest.save request) {
        return placeService.save(request);
    }
}
