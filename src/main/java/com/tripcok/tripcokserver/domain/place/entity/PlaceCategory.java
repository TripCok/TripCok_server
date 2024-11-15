package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class PlaceCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_category_id")
    private Long id;

    /* 카테고리 이름 */
    private String name;

    /* 부모 카테고리 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PlaceCategory parentCategory;

    /* 자식 카테고리 */
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceCategory> childCategories;

    /* 깊이 */
    private Integer depth;

}
