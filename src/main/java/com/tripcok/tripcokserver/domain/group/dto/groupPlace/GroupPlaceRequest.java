package com.tripcok.tripcokserver.domain.group.dto.groupPlace;

import lombok.Data;

@Data
public class GroupPlaceRequest {
    private Long memberId; // 요청자 아이디
    private Long groupId; // 그룹 아이디
    private Long placeId; // 장소 아이디
}
