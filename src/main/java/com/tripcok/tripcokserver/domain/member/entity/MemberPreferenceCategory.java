package com.tripcok.tripcokserver.domain.member.entity;

import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import jakarta.persistence.*;

@Entity
public class MemberPreferenceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaceCategory placeCategory;

    public MemberPreferenceCategory(Member member, PlaceCategory placeCategory) {
        this.member = member;
        this.placeCategory = placeCategory;
    }

    public MemberPreferenceCategory() {

    }
}
