package com.tripcok.tripcokserver.domain.group.controller;

import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceRequest;
import com.tripcok.tripcokserver.domain.group.service.GroupPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/group/place")
@RequiredArgsConstructor
public class GroupPlaceController {

    private final GroupPlaceService groupPlaceService;

    /* 그룹에 여행지 추가 */
    @PostMapping
    public ResponseEntity<?> groupAddPlace(@RequestBody GroupPlaceRequest groupPlaceRequest) {
        return groupPlaceService.groupAddPlace(groupPlaceRequest);
    }

    /* 그룹의 여행지 조회 - 모두 조회 */
    @GetMapping("/all")
    public ResponseEntity<?> groupAllGroupInPlaces(
            @RequestParam("size") Integer pageSize,
            @RequestParam("page") Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return groupPlaceService.getGroupInPlace(pageable);
    }


    /* 그룹의 여행지 조회 - 단일 조회 */

    /* 그룹에 여행지 삭제 */

    /* 그룹에 여행지 순서 변경 */
}
