package com.tripcok.tripcokserver.domain.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
public class PlaceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String fileName;

    private String imagePath;

    @Setter
    @Enumerated(EnumType.STRING)
    private PlaceImageType imageType;

    public PlaceImage(String fileName, String filePath) {
        this.fileName = fileName;
        this.imagePath = filePath;
        this.imageType = PlaceImageType.C;
    }

    public PlaceImage() {

    }
}
