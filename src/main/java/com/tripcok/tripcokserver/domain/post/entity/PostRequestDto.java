package com.tripcok.tripcokserver.domain.post.entity;

import com.tripcok.tripcokserver.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequestDto {

    private String title; // 게시물 제목
    private String content; // 게시물 내용
    private Type Type;

}
