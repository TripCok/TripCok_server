package com.tripcok.tripcokserver.domain.group.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 그룹 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    /* 멤버 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /* 멤버의 역할 */
    @Enumerated(EnumType.STRING)
    private GroupRole role;

    /* 초대 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus invitationStatus = InvitationStatus.PENDING;

    /* 상태 변경 */
    public void acceptInvitation() {
        this.invitationStatus = InvitationStatus.ACCEPTED;
    }

    public void rejectInvitation() {
        this.invitationStatus = InvitationStatus.REJECTED;
    }

    public GroupMember(Member member, Group group, GroupRole role) {
        this.group = group;
        this.member = member;
        this.role = role;
        this.invitationStatus = InvitationStatus.PENDING;
    }

}
