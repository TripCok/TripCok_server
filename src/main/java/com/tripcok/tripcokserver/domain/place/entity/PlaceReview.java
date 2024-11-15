package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class PlaceReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 리뷰 제목 */
    private String title;

    /* 리뷰 내용 */
    private String content;

    /* 대상 장소 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    /* 리뷰 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    /* 좋아요 */
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceReviewLike> likes;
}

