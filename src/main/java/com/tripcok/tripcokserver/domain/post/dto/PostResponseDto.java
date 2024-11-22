package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.domain.post.entity.Type;
import lombok.Data;
import org.hibernate.query.Page;

import java.util.Optional;


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


    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.type = post.getType();
    }
}
