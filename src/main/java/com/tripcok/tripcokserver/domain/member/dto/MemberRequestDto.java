package com.tripcok.tripcokserver.domain.member.dto;

import com.tripcok.tripcokserver.domain.member.entity.Gender;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberRequestDto {

    /* 회원 가입 */
    @Data
    public static class save {
        private String name;
        private String email;
        private String password;
        private String phone;
        private LocalDate birthday;
        private String profileImage;
        private Gender gender;
        private String address;
    }

    /* 로그인 */
    @Data
    public static class login {
        private String email;
        private String password;
    }

    /* 회원 정보 수정 */
    @Data
    public static class update {
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
    }

}
