package com.tripcok.tripcokserver.domain.file;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class FileDto {

    private String name;
    private String path;

    public FileDto(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
