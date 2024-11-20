package com.tripcok.tripcokserver.domain.group.service;

import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceRequest;
import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceResponse;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupPlace;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.group.repository.GroupMemberRepository;
import com.tripcok.tripcokserver.domain.group.repository.GroupPlaceRepository;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupPlaceService {

    private final GroupPlaceRepository groupPlaceRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PlaceRepository placeRepository;

    public ResponseEntity<?> groupAddPlace(GroupPlaceRequest request) {

        GroupMember groupMember;
        /* 해당 그룹(request.getGroupId)의 맴버(request.getMemberId)의 권한 체크 */

        try {
            groupMember = checkMemberGroupInRole(request.getMemberId(), request.getGroupId());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("여행지를 추가할 수 있는 권한이 없습니다.");
        }

        /* 해당 그룹에 추가된 여행지가 있는지 없는지 검사 */
        List<GroupPlace> groupPlaces = groupPlaceRepository.findByGroup_IdOrderByOrdersDesc(request.getGroupId());

        /* 여행지 추가 할때 순서 정의 */
        int order = 1;

        if (!groupPlaces.isEmpty()) {
            order = groupPlaces.get(0).getOrders() + 1;
        }

        /* 여행지 추출 */
        Place place = placeRepository.findById(request.getPlaceId()).orElseThrow(
                () -> new EntityNotFoundException("추가하려는 여행지가 없습니다. placeId = " + request.getPlaceId())
        );

        /* 여행지 추가 */
        GroupPlace groupPlace = new GroupPlace(groupMember.getGroup(), place, order);
        GroupPlace save = groupPlaceRepository.save(groupPlace);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GroupPlaceResponse(save));
    }

    private GroupMember checkMemberGroupInRole(Long requestMemberId, Long groupId) throws AccessDeniedException {

        return groupMemberRepository.findByGroupInAdminMember(groupId, GroupRole.ADMIN, requestMemberId).orElseThrow(
                () -> new AccessDeniedException(
                        "여행지를 추가할 수 있는 권한이 없습니다. requestMemberId = : " + requestMemberId
                                + ", groupId = : " + groupId
                )
        );
    }

    public ResponseEntity<?> getGroupInPlace(Pageable pageable) {
        Page<GroupPlace> all = groupPlaceRepository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(all.map(GroupPlaceResponse::new));
    }
}
