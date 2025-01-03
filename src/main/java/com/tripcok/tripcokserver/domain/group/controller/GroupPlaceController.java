package com.tripcok.tripcokserver.domain.group.controller;

import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceRequest;
import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceUpdateRequest;
import com.tripcok.tripcokserver.domain.group.service.GroupPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/group/place")
@RequiredArgsConstructor
public class GroupPlaceController {

    private final GroupPlaceService groupPlaceService;

    /* 모임에 여행지 추가 */
    @PostMapping
    public ResponseEntity<?> groupAddPlace(@RequestBody GroupPlaceRequest groupPlaceRequest) {
        return groupPlaceService.groupAddPlace(groupPlaceRequest);
    }

    /* 모임의 여행지 조회 - 모두 조회 */
    @GetMapping("/all")
    public ResponseEntity<?> groupAllGroupInPlaces(
            @RequestParam("groupId") Long groupId,
            @RequestParam("size") Integer pageSize,
            @RequestParam("page") Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return groupPlaceService.getGroupInPlace(groupId, pageable);
    }

    /* 모임에 여행지 삭제 */
    @DeleteMapping
    public ResponseEntity<?> groupRemovePlace(
            @RequestParam("placeId") Long placeId,
            @RequestParam("groupId") Long groupId,
            @RequestParam("memberId") Long memberId) {
        return groupPlaceService.groupInPlaceRemove(placeId, groupId, memberId);
    }

    /* 모임에 여행지 순서 변경 */
    @PutMapping("/orders")
    public ResponseEntity<?> groupInPlaceUpdateOrders(@RequestBody GroupPlaceUpdateRequest groupPlaceUpdateRequests) throws AccessDeniedException {
        return groupPlaceService.groupInPlaceUpdateOrders(groupPlaceUpdateRequests);
    }
}
