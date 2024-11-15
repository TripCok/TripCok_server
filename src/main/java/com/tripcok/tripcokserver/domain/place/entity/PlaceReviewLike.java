package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PlaceReviewLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 좋아요한 리뷰 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private PlaceReview review;

    /* 좋아요한 사용자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /* 좋아요 일자 */
    private LocalDateTime likedAt;
}
