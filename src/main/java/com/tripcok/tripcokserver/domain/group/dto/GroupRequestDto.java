package com.tripcok.tripcokserver.domain.group.dto;

import lombok.Data;

@Data
public class GroupRequestDto {

    /* 모임 생성 */
    @Data
    public static class save {
        private Long id;
        private String groupName;
        private String description;
        private String category;
    }

    /* 모임 수정 */
    @Data
    public static class update {
        private Long id;
        private String groupName;
        private String description;
        private String category;
    }
}
