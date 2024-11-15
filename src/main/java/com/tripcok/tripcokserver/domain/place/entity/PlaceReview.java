package com.tripcok.tripcokserver.domain.place.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class PlaceReview {

    @Id
    @GeneratedValue()
    private Long id;

    /* 제목 */
    private String title;

    /* 내용 */
    private String content;

    /* 좋아유 */
    @OneToMany
    private List<Member> likeMembers;
}
