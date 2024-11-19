package com.tripcok.tripcokserver.domain.board.dto;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardRequestDto {
    public Member member;
    public String title;
    public String content;
    public Long groupId;
}
