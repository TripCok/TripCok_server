package com.tripcok.tripcokserver.domain.application.dto;

import com.tripcok.tripcokserver.domain.application.entity.Application;
import lombok.Data;

@Data
public class ApplicationResponseDto {

    private Long id;
    private Long memberId;
    private String memberName;
    private String memberProfile;

    private Long groupId;
    private String groupName;

    public ApplicationResponseDto(Application application) {
        this.id = application.getId();
        this.memberId = application.getMember().getId();
        this.memberName = application.getMember().getName();
        this.memberProfile = application.getMember().getProfileImage();
        this.groupId = application.getGroup().getId();
        this.groupName = application.getGroup().getGroupName();
    }
}