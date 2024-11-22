package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Type;
import lombok.Data;


@Data
public class PostResponseDto {

    public String message;
    public Long postId;
    public Type type;


    public PostResponseDto(String message, Long postId, Type type) {
        this.message = message;
        this.postId = postId;
        this.type = type;
    }
}
