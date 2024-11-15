package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PlaceSubscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 장소 이름 */
    @OneToOne(mappedBy = "place_id")
    private Place place;

    /* 사용자 이름 */
    @OneToOne(mappedBy = "member_id")
    private Member member;

}
