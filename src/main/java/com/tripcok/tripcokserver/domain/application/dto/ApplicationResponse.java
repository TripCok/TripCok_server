package com.tripcok.tripcokserver.domain.application.dto;

import lombok.Data;

@Data
public class ApplicationResponse {

    /* 모임 신청 완료 */
    @Data
    public static class complete {
        private String message;
        private String group_id;
    }

}
