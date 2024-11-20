package com.tripcok.tripcokserver.domain.board.entity;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name="board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 게시판 제목

    @OneToOne
    @JoinColumn(name = "group_id", unique = true)
    private Group group;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

}
