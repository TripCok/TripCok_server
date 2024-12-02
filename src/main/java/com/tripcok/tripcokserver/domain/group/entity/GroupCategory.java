package com.tripcok.tripcokserver.domain.group.entity;

import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GroupCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 여행지 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    /* 카테고리 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PlaceCategory category;

    public GroupCategory(Group group, PlaceCategory category) {
        this.group = group;
        this.category = category;
    }

    public GroupCategory() {

    }
}
