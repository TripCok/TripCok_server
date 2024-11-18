package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto.save;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("PlaceService 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaceServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PlaceCategoryRepository placeCategoryRepository;

    @Autowired
    private PlaceService placeService;

    private Long adminMemberId;
    private Long commonMemberId;
    private Long rootCategoryId;
    private Long childCategoryId;

    @BeforeEach
    @DisplayName("테스트 데이터 준비")
    void setup() {
        // 관리자 사용자 생성
        save adminMember = new save();
        adminMember.setName("관리자");
        adminMember.setEmail("admin@tripcok.com");
        adminMember.setPassword("123456");
        Member member = new Member(adminMember);
        member.setRole(Role.MANAGER);
        Member savedAdmin = memberRepository.save(member);
        adminMemberId = savedAdmin.getId();

        // 일반 사용자 생성
        save commonMember = new save();
        commonMember.setName("일반 사용자");
        commonMember.setEmail("common@tripcok.com");
        commonMember.setPassword("123456");
        Member savedCommon = memberRepository.save(new Member(commonMember));
        commonMemberId = savedCommon.getId();

        // 루트 카테고리 생성
        PlaceCategoryRequest rootCategoryRequest = new PlaceCategoryRequest();
        rootCategoryRequest.setMemberId(adminMemberId);
        rootCategoryRequest.setName("여행지 유형");
        PlaceCategory rootCategory = placeCategoryRepository.save(new PlaceCategory(rootCategoryRequest));
        rootCategoryId = rootCategory.getId();

        // 자식 카테고리 생성
        PlaceCategoryRequest childCategoryRequest = new PlaceCategoryRequest();
        childCategoryRequest.setMemberId(adminMemberId);
        childCategoryRequest.setName("해변");
        childCategoryRequest.setParentId(rootCategoryId);
        PlaceCategory childCategory = placeCategoryRepository.save(new PlaceCategory(childCategoryRequest));
        childCategoryId = childCategory.getId();
    }

    @Test
    @DisplayName("여행지 생성 성공 테스트")
    void createPlaceSuccessTest() throws AccessDeniedException {
        // 여행지 생성 요청
        PlaceRequest.save placeRequest = new PlaceRequest.save();
        placeRequest.setName("해운대");
        placeRequest.setMemberId(adminMemberId);
        placeRequest.setAddress("test-address");
        placeRequest.setDescription("test-description");
        placeRequest.setStartTime(LocalTime.of(8, 0));
        placeRequest.setEndTime(LocalTime.of(20, 0));
        placeRequest.setCategoryIds(List.of(childCategoryId));

        ResponseEntity<?> response = placeService.savePlace(placeRequest);

        // 검증
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PlaceResponse placeResponse = (PlaceResponse) response.getBody();
        assertNotNull(placeResponse);
        assertEquals("해운대", placeResponse.getName());
        assertEquals(1, placeResponse.getCategories().size());
    }

    @Test
    @DisplayName("권한 없는 사용자에 의한 여행지 생성 실패 테스트")
    void createPlaceFailTest() {
        // 여행지 생성 요청
        PlaceRequest.save placeRequest = new PlaceRequest.save();
        placeRequest.setName("해운대");
        placeRequest.setMemberId(commonMemberId);
        placeRequest.setAddress("test-address");
        placeRequest.setDescription("test-description");
        placeRequest.setStartTime(LocalTime.of(8, 0));
        placeRequest.setEndTime(LocalTime.of(20, 0));
        placeRequest.setCategoryIds(List.of(childCategoryId));

        // 예외 발생 검증
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            placeService.savePlace(placeRequest);
        });
    }

    @Test
    @DisplayName("유효하지 않은 카테고리 ID로 여행지 생성 실패 테스트")
    void createPlaceInvalidCategoryTest() {
        // 여행지 생성 요청
        PlaceRequest.save placeRequest = new PlaceRequest.save();
        placeRequest.setName("해운대");
        placeRequest.setMemberId(adminMemberId);
        placeRequest.setAddress("test-address");
        placeRequest.setDescription("test-description");
        placeRequest.setStartTime(LocalTime.of(8, 0));
        placeRequest.setEndTime(LocalTime.of(20, 0));
        placeRequest.setCategoryIds(List.of(999L)); // 존재하지 않는 ID

        // 예외 발생 검증
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            placeService.savePlace(placeRequest);
        });
    }
}
