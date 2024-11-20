package com.tripcok.tripcokserver.domain.group.entity;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import jakarta.persistence.*;

@Entity
public class GroupMemberInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /* 초대 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus invitationStatus = InvitationStatus.PENDING;

    public GroupMemberInvite(Member member, Group group) {
        this.member = member;
        this.group = group;
    }

    public GroupMemberInvite() {

    }

    /* 초대 거절 */
    public void inviteReject() {
        this.invitationStatus = InvitationStatus.REJECTED;
    }

}
