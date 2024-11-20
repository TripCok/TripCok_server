package com.tripcok.tripcokserver.domain.member.entity;

import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.place.entity.PlaceReview;
import com.tripcok.tripcokserver.domain.place.entity.PlaceReviewLike;
import com.tripcok.tripcokserver.domain.place.entity.PlaceSubscribe;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Table(name = "member")
@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 사용자 이름 */
    private String name;

    /* 이메일 */
    private String email;

    /* 비밀번호 */
    private String password;

    /* 휴대폰 번호 */
    private String phone;

    /* 생년월일 */
    private LocalDate birthday;

    /* 프로필 이미지 */
    private String profileImage;

    /* 성별 */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /* 주소 */
    private String address;

    /* 역할 */
    @Enumerated(EnumType.STRING)
    private Role role;

    /* 모임 */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupMember> groupMembers;

    /* 구독 */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<PlaceSubscribe> subscribes;

    /* 좋아요한 리뷰 */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<PlaceReviewLike> reviewLikes;

    /* 작성한 리뷰 */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<PlaceReview> reviews;

    /* 사용자가 작성한 모임 */



    /* 회원가입 */
    public Member(MemberRequestDto.save member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.phone = member.getPhone();
        this.birthday = member.getBirthday();
        this.profileImage = member.getProfileImage();
        this.gender = member.getGender();
        this.address = member.getAddress();
        this.role = Role.USER;
    }

    /* 회원 정보 수정 */
    public Member update(MemberRequestDto.update memberRequest) {
        this.name = memberRequest.getName();
        this.email = memberRequest.getEmail();
        this.password = memberRequest.getPassword();
        this.phone = memberRequest.getPhone();
        this.birthday = memberRequest.getBirthday();
        this.profileImage = memberRequest.getProfileImage();
        this.gender = memberRequest.getGender();
        this.address = memberRequest.getAddress();
        this.role = memberRequest.getRole();
        return this;
    }

    public Member() {
    }
}


