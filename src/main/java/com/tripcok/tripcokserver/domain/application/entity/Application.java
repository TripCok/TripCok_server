package com.tripcok.tripcokserver.domain.application.entity;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id")
    private String id;

    /* Member 정보 */
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    private List<ApplicationMember> applicationMembers;

    private Long memberId;

    /* 그룹 당 N 개 신청서 */
    @OneToOne
    private Group group;

    /* 신청서 제목*/
    private String title;

    /* 진행 과정 */
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
