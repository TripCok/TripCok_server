package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Type;
import lombok.Data;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;

@Data
public class PostRequestDto {

    private String title; // 게시물 제목
    private String content; // 게시물 내용
    private Type type;

    public static class posts{
        private Pageable pageable;
    }
}
