package com.tripcok.tripcokserver.domain.application.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "application_member")
public class ApplicationMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // Member의 외래키
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id") // Application의 외래키
    private Application application;
    private String role;
}

