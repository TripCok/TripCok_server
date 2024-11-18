package com.tripcok.tripcokserver.domain.application.entity;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;


@Entity
@Getter
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id")
    private Long id;

    /* Member 정보 */
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationMember> applicationMembers;

    /* 그룹 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    /* 신청서 제목 */
    private String title;

    /* 신청서 내용 */
    private String description;

    /* 진행 과정 */
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
