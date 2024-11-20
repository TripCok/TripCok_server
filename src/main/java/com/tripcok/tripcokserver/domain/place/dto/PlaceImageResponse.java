package com.tripcok.tripcokserver.domain.place.dto;

import com.tripcok.tripcokserver.domain.place.entity.PlaceImage;
import lombok.Data;

@Data
public class PlaceImageResponse {
    private Long id;
    private String fileName;     // 이미지 파일 이름
    private String imagePath;    // 이미지 파일 경로
    private String imageType;    // 이미지 타입 (예: 'C', 'B' 등)

    public PlaceImageResponse(PlaceImage image) {
        this.id = image.getId();
        this.fileName = image.getFileName();
        this.imagePath = image.getImagePath();
        this.imageType = image.getImageType().name();
    }
}
