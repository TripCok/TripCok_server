package com.tripcok.tripcokserver.domain.place.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Data
public class PlaceRequest {

    @Data
    public static class save {

        /* 장소 등록자 */
        private Long memberId;

        /* 장소 이름 */
        private String name;

        /* 장소 설명 */
        private String description;

        /* 장소 주소 */
        private String address;

        /* 운영 시간 */
        private LocalTime startTime;
        private LocalTime endTime;

        /* 카테고리 아이디 리스트*/
        private List<Long> categoryIds;

        /* 장소 사진 */
        private List<MultipartFile> imageFiles;
    }
}
