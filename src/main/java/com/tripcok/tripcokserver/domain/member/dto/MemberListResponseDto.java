package com.tripcok.tripcokserver.domain.member.dto;

import com.tripcok.tripcokserver.domain.member.entity.Gender;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MemberListResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate birthDay;
    private String address;
    private String profileImg;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    public MemberListResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.address = member.getAddress();
        this.birthDay = member.getBirthday();
        this.gender = member.getGender();
        this.profileImg = member.getProfileImage();
        this.createdDate = member.getCreateTime();
        this.updatedDate = member.getUpdateTime();
    }
}
