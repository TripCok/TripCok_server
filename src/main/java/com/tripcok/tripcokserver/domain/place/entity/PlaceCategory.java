package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryRequest;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class PlaceCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PlaceCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceCategory> childCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceCategoryMapping> categoryMappings;

    private Integer depth;

    public PlaceCategory(PlaceCategoryRequest request) {
        this.name = request.getPlaceName();
        this.depth = 0; // 기본 값, setParentCategory 호출 시 업데이트
    }

    public void setParentCategory(PlaceCategory parent) {
        this.parentCategory = parent;
        this.depth = parent.getDepth() + 1;
        if (!parent.getChildCategories().contains(this)) {
            parent.getChildCategories().add(this);
        }
    }

    public PlaceCategory() {

    }
}
