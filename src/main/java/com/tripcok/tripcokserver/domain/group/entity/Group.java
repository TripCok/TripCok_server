package com.tripcok.tripcokserver.domain.group.entity;

import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private long id;

    /* 그룹 이름 */
    private String groupName;

    /* 그룹장 */
    @OneToOne
    @JoinColumn(name = "group_leader_id")
    private Member member;

    /* 그룹 내 인원 */
    @ManyToMany(mappedBy = "groups")
    private List<Member> memberList;

    /* 그룹 1 - N 게시물 */
    /* 그룹 한개에 N개 보드 */
    @OneToMany(mappedBy = "board")
    private List<Board> boardlist;

    /* 설명 */
    private String description;

    /* 카테고리 */
    private String category;

    /* 신청서 */
    @ManyToMany(mappedBy = "groups")
    private List<Application> applications;


}
