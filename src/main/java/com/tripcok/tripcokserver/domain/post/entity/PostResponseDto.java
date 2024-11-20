package com.tripcok.tripcokserver.domain.post.entity;

import lombok.Data;

import java.util.Optional;

@Data
public class PostResponseDto {
    public String message;
    public Long postId;

    public PostResponseDto(String message, Long postId) {
        this.message = message;
        this.postId = postId;
    }
}
