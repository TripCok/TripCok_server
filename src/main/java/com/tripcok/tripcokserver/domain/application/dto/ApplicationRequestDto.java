package com.tripcok.tripcokserver.domain.application.dto;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import lombok.Data;

@Data
public class ApplicationRequestDto {

    /* 모임 신청 */
    @Data
    public static class save {
        private Long member_id;
        private Long group_id;
        private Group group;
        private String title;
        private String description;
    }
}
