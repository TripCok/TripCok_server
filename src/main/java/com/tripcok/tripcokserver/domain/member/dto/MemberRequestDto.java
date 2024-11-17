package com.tripcok.tripcokserver.domain.member.dto;

import com.tripcok.tripcokserver.domain.member.entity.Gender;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberRequestDto {

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

    @Data
    public static class login {
        private String email;
        private String password;
    }

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
