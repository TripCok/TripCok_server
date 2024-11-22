package com.tripcok.tripcokserver.domain.postcomment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentResponseDto {
    public String message;
    public Long Id;

}
