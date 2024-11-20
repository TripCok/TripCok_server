package com.tripcok.tripcokserver.domain.application.dto;

import lombok.Data;

@Data
public class ApplicationRequestDto {

    /* 모임 신청 */
    @Data
    public static class applicationsave {
        private Long member_id;
        private Long group_id;
    }

    /* 모임 가입 완료 */
    @Data
    public static class applicationaccept {
        private Long applicationId;
    }

}