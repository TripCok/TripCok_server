package com.tripcok.tripcokserver.domain.member.dto;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import lombok.Data;

@Data
public class MemberListResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String profileImg;

    public MemberListResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.address = member.getAddress();
        this.profileImg = member.getProfileImage();
    }
}
