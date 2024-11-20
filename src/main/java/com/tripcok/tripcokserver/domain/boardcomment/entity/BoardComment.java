package com.tripcok.tripcokserver.domain.boardcomment.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "comment")
public class BoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content; // 댓글 내용

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 댓글이 속한 게시물

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 댓글 작성자

}