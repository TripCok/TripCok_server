package com.tripcok.tripcokserver.domain.application.dto;

import lombok.Data;

@Data
public class ApplicationRequestDto {

    /* 모임 신청 */
    @Data
    public static class applicationSave {
        private Long memberId;
        private Long groupId;
    }

    /* 모임 가입 완료 */
    @Data
    public static class applicationAccept {
        private Long applicationId;
        private Long groupAdminId;
    }

}