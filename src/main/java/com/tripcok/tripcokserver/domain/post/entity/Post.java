package com.tripcok.tripcokserver.domain.post.entity;

import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.boardcomment.entity.BoardComment;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardComment> comments;

    public Post(PostRequestDto requestDto, Board board) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.board = board;
    }

    public void addComment(BoardComment comment) {
        comments.add(comment);
    }

}