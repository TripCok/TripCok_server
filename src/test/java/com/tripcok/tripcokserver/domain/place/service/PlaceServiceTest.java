package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto.save;

@SpringBootTest
class PlaceServiceTest {

    private MemberRepository memberRepository;

    PlaceServiceTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @BeforeEach
    @DisplayName("사용자 생성")
    void createMember() {

        save newMember = new save();
        newMember.setName("test");
        newMember.setEmail("test@tripcok.com");
        newMember.setPassword("123456");

        Member member = new Member(newMember);
        member.setRole(Role.MANAGER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("여행지 생성")
    void savePlace() {

        /* 여행지 생성 */
        PlaceRequest.save placeRequest = new PlaceRequest.save();
        placeRequest.setName("test-place");
        placeRequest.setMemberId(1L);
        placeRequest.setAddress("test-address");
        placeRequest.setDescription("test-description");


    }
}