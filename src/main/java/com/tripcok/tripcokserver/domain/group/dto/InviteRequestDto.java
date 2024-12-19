package com.tripcok.tripcokserver.domain.group.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRequestDto {
    private Long inviteId;
    private Long memberId;
    private boolean action; // true: 수락
}
