package com.tripcok.tripcokserver.domain.group.dto;

import lombok.Data;

@Data
public class GroupInviteDto {
    private Long groupId;
    private Long groupAdminId;
    private String email;
}
