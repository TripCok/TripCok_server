package com.tripcok.tripcokserver.domain.board.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "board_category")
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_category_id")
    private Long id;

    /* 카테고리 이름 */
    private String name;

    /* 부모 카테고리 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardCategory parentCategory;

    /* 자식 카테고리 */
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardCategory> childCategories;

    /* 깊이 */
    private Integer depth;
}