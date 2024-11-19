package com.tripcok.tripcokserver.domain.board.entity;

import com.tripcok.tripcokserver.domain.application.entity.ApplicationMember;
import com.tripcok.tripcokserver.domain.board.dto.BoardRequestDto;
import com.tripcok.tripcokserver.domain.board.dto.BoardResponseDto;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@RequiredArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    /* 제목 */
    private String title;

    /* 내용 */
    private String content;

    /* 조회수 */
    private Integer views;

    /* 썸네일 */
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private Type type;

    /* 글 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래 키
    private Member member; // 'Member'와 관계 설정

    /* 그룹 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Board(BoardRequestDto requestDto, Group group, Member member) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.group = group;
        this.member = member;
    }
}

