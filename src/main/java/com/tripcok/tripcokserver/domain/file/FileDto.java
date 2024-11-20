package com.tripcok.tripcokserver.domain.file;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class FileDto {

    private String name;
    private String path;

}
