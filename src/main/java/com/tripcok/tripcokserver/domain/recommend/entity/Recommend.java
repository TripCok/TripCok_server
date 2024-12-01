package com.tripcok.tripcokserver.domain.recommend.entity;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.recommend.dto.RecommendRequestDto;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "recommend")
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name ="group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @JoinColumn(name = "place_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private Long score;

    public Recommend(RecommendRequestDto requestDto, Group group, Place place){
        this.score = requestDto.getScore();
        this.group = group;
        this.place = place;
    }

    public Recommend() {

    }
}
