package com.tripcok.tripcokserver.domain.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class PlaceRequest {

    @Data
    @Schema(description = "요청 데이터(JSON)")
    public static class placeSave {

        @NotNull
        private Long memberId;

        @NotNull
        private String name;

        @NotNull
        private String description;

        @NotNull
        private String address;

        /* 운영 시간 */
        @NotNull
        private String strStartTime;

        @NotNull
        private String strEndTime;

        private LocalTime startTime;
        private LocalTime endTime;

        private List<Long> categoryIds;

        // JSON 데이터를 LocalTime으로 변환
        public void convertToLocalTime() {
            this.startTime = LocalTime.parse(strStartTime);
            this.endTime = LocalTime.parse(strEndTime);
        }
    }

}
