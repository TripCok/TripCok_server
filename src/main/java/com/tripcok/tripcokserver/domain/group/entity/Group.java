package com.tripcok.tripcokserver.domain.group.entity;

import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "groups")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private long id;

    /* 그룹 이름 */
    private String groupName;

    /* 그룹 내 인원 */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> groupMembers;

    /* 그룹 1 - N 게시물 */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boardlist;

    /* 설명 */
    private String description;

    /* 카테고리 */
    private String category;

    /* 신청서 */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

}


