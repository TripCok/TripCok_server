package com.tripcok.tripcokserver.domain.board.entity;

import com.tripcok.tripcokserver.domain.application.entity.ApplicationMember;
import com.tripcok.tripcokserver.domain.board.dto.BoardRequestDto;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    /* 멤버 */
    @OneToMany(mappedBy = "board", fetch =FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMembers;

    /* 그룹 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;


}

