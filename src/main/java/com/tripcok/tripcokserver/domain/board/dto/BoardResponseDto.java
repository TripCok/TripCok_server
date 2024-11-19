package com.tripcok.tripcokserver.domain.board.dto;

import com.tripcok.tripcokserver.domain.board.entity.Board;

public class BoardResponseDto {
    public String title;
    public String message;

    public void save(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
