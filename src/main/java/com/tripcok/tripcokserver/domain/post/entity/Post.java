package com.tripcok.tripcokserver.domain.post.entity;

import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto.put;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import com.tripcok.tripcokserver.domain.post.dto.PostRequestDto;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="post")
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 게시물 제목

    @Column(nullable = false)
    private String content; // 게시물 내용

    @Enumerated(EnumType.STRING)
    private Type type = Type.COMMON;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostComment> comments = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
    }

    public Post(PostRequestDto.create requestDto, Board board, Member member) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.board = board;
        this.member = member;
        if (requestDto.getType() == Type.NOTICE) {
            this.type = Type.NOTICE;
        }
    }

    public void addComment(PostComment comment) {
        comments.add(comment);
    }

    public void updatePost(PostRequestDto.put requestDto){
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : title;
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
        this.type = requestDto.getType() != null ? requestDto.getType() : type;
    }
}