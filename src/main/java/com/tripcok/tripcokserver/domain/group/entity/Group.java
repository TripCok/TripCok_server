package com.tripcok.tripcokserver.domain.group.entity;

import com.tripcok.tripcokserver.domain.application.entity.Application;
import com.tripcok.tripcokserver.domain.board.Board;
import com.tripcok.tripcokserver.domain.group.dto.GroupRequestDto;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private long id;

    /* 그룹 이름 */
    private String groupName;

    /* 그룹 내 인원 */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupMember> groupMembers;

    /* 그룹 1 - N 게시물 */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", unique = true)
    private Board board;

    /* 설명 */
    private String description;

    /* 카테고리 */
    private String category;

    /* 신청서 */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    public void addApplication(Application application) {
        applications.add(application);
    }


    /* 구인 상태 */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean recruiting = true; // 기본값은 구인 중~

    /* 모암 초대 인원 */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMemberInvite> groupMemberInvites;

    public Boolean isRecruiting() {

        return recruiting;
    }

    public void setRecruiting(Boolean recruiting) {

        this.recruiting = recruiting;
    }

    public Group(GroupRequestDto requestDto, Board board) {

        this.groupName = requestDto.getGroupName();
        this.description = requestDto.getDescription();
        this.category = requestDto.getCategory();
        this.board = board;

    }

}


