package com.tripcok.tripcokserver.domain.group.dto.groupPlace;


import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class GroupPlaceUpdateRequest {

    private Long memberId;
    private Long groupId;
    List<GroupPlace> groupPlaceList;

    @Getter
    public static class GroupPlace {
        private Long groupPlaceId;
        private Integer orders;
    }
}
