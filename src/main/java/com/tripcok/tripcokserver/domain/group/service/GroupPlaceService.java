package com.tripcok.tripcokserver.domain.group.service;

import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceRequest;
import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceResponse;
import com.tripcok.tripcokserver.domain.group.dto.groupPlace.GroupPlaceUpdateRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupPlaceService {

    private final GroupPlaceRepository groupPlaceRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PlaceRepository placeRepository;

    /* 그룹에 여행지 추가 */
    @Transactional
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

    /* 그룹의 추가된 여행지 조회 */
    public ResponseEntity<?> getGroupInPlace(Long groupId, Pageable pageable) {
        Page<GroupPlace> all = groupPlaceRepository.findByGroup_IdOrderByOrdersAsc(groupId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(all.map(GroupPlaceResponse::new));
    }

    /* 그룹에 추가된 여행지 삭제 */
    public ResponseEntity<?> groupInPlaceRemove(Long id) {
        GroupPlace groupPlace = groupPlaceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("찾을수 없는 그룹에 포함 된 여행지 입니다. id = " + id)
        );

        groupPlaceRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 "
                + groupPlace.getPlace().getName()
                + " 여행지를 삭제하였습니다.");
    }

    /* 그룹에 추가된 여행지 순서 조절 */
    /* EntityNotFoundException이 발생하면 이전 업데이트한 순서 다시 Rollback */
    @Transactional(rollbackFor = {EntityNotFoundException.class})
    public ResponseEntity<?> groupInPlaceUpdateOrders(GroupPlaceUpdateRequest groupPlaceUpdateRequests) throws AccessDeniedException {
        // 그룹 권한 체크
        checkMemberGroupInRole(groupPlaceUpdateRequests.getMemberId(), groupPlaceUpdateRequests.getGroupId());

        try {
            // 모든 요청된 여행지에 대해 순서 업데이트
            for (GroupPlaceUpdateRequest.GroupPlace groupPlaceUpdateRequest : groupPlaceUpdateRequests.getGroupPlaceList()) {
                GroupPlace groupPlace = groupPlaceRepository.findById(groupPlaceUpdateRequest.getGroupPlaceId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                groupPlaceUpdateRequest.getGroupPlaceId() + "의 정보를 찾을 수 없습니다."
                        ));
                groupPlace.updateOrder(groupPlaceUpdateRequest.getOrders());
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("페이지를 새로 고침 후 다시 실행 해주세요.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 여행지를 수정하였습니다.");
    }

}
