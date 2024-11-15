package com.tripcok.tripcokserver.domain.member.entity;

import com.tripcok.tripcokserver.domain.application.entity.ApplicationMember;
import com.tripcok.tripcokserver.domain.group.controller.GroupMember;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
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

    /* 그룹 참여 */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> groupMembers;

    /* 신청서 멤버 */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationMember> applicationMembers;
}


