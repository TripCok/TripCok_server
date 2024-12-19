package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import lombok.Data;

@Data
public class GroupMemberResponse {
    private Long id;
    private String name;
    private String email;
    private String profileImage;

    public GroupMemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.profileImage = member.getProfileImage();
    }
}
