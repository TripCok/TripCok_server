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

        public void convertToLocalTime() {
            this.startTime = LocalTime.parse(strStartTime);
            this.endTime = LocalTime.parse(strEndTime);
        }
    }

    @Data
    @Schema(description = "여행지 수정 요청 데이터")
    public static class placeUpdate {
        @NotNull
        private Long memberId; // 요청한 사용자 ID

        private String name;
        private String description;
        private String address;

        /* 운영 시간 */
        private String strStartTime;
        private String strEndTime;

        private LocalTime startTime;
        private LocalTime endTime;

        private List<Long> categoryIds;

        public void convertToLocalTime() {
            if (strStartTime != null) {
                this.startTime = LocalTime.parse(strStartTime);
            }
            if (strEndTime != null) {
                this.endTime = LocalTime.parse(strEndTime);
            }
        }
    }
}
