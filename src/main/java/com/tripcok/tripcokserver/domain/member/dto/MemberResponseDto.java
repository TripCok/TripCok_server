package com.tripcok.tripcokserver.domain.member.dto;

import com.tripcok.tripcokserver.domain.member.entity.Gender;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponseDto {

    @Data
    public static class Info {
        private String name;
        private String email;
        private String password;
        private String phone;
        private LocalDate birthday;
        private String profileImage;
        private Gender gender;
        private String address;
        private Role role;

        public Info(Member member) {
            this.name = member.getName();
            this.email = member.getEmail();
            this.password = member.getPassword();
            this.phone = member.getPhone();
            this.birthday = member.getBirthday();
            this.profileImage = member.getProfileImage();
            this.gender = member.getGender();
            this.address = member.getAddress();
            this.role = member.getRole();
        }
    }
}

