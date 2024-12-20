package com.tripcok.tripcokserver.domain.member.dto;

import com.tripcok.tripcokserver.domain.member.entity.Gender;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.PreferCategory;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponseDto {

    /* 회원 정보 조회 */
    @Data
    public static class Info {
        private Long id;
        private String name;
        private String email;
        private String password;
        private String phone;
        private LocalDate birthday;
        private String profileImage;
        private Gender gender;
        private String address;
        private Role role;
        private PreferCategory isPreferCategory;

        /* 회원 정보 수정 */
        public Info(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.email = member.getEmail();
            this.password = member.getPassword();
            this.phone = member.getPhone();
            this.birthday = member.getBirthday();
            this.profileImage = member.getProfileImage();
            this.gender = member.getGender();
            this.address = member.getAddress();
            this.role = member.getRole();
            this.isPreferCategory = member.getIsPreferCategory();
        }
    }
}

