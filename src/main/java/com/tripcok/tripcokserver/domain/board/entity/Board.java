package com.tripcok.tripcokserver.domain.board.entity;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id", unique = true)
    private Group group;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    public void addPosts(Post post) {
        posts.add(post);
    }
    public void addGroup(Group group){
        this.group = group;
    }
}
