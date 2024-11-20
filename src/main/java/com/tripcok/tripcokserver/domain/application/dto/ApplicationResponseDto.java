package com.tripcok.tripcokserver.domain.application.dto;

import lombok.Data;

@Data
public class ApplicationResponseDto {

    /* 모임 신청 완료 */
    @Data
    public static class applicationcomplete {
        private String message;
        private String group_id;
    }

}