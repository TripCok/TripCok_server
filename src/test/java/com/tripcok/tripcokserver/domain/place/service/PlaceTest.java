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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
class PlaceTest {

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
    private Long files;



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
        rootCategoryRequest.setPlaceName("여행지 유형");
        PlaceCategory rootCategory = placeCategoryRepository.save(new PlaceCategory(rootCategoryRequest));
        rootCategoryId = rootCategory.getId();

        // 자식 카테고리 생성
        PlaceCategoryRequest childCategoryRequest = new PlaceCategoryRequest();
        childCategoryRequest.setMemberId(adminMemberId);
        childCategoryRequest.setPlaceName("해변");
        childCategoryRequest.setParentId(rootCategoryId);
        PlaceCategory childCategory = placeCategoryRepository.save(new PlaceCategory(childCategoryRequest));
        childCategoryId = childCategory.getId();
    }

    @Test
    @DisplayName("여행지 생성 성공 테스트")
    void createPlaceSuccessTest() throws AccessDeniedException {
        // 여행지 생성 요청
        PlaceRequest.placeSave placeRequest = new PlaceRequest.placeSave();
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
        PlaceRequest.placeSave placeRequest = new PlaceRequest.placeSave();
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
        PlaceRequest.placeSave placeRequest = new PlaceRequest.placeSave();
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

    @Test
    @DisplayName("여행지 상세 조회 테스트")
    void getPlaceDetailsTest() throws AccessDeniedException {

        // 여행지 생성 요청
        PlaceRequest.placeSave placeRequest = new PlaceRequest.placeSave();
        placeRequest.setName("해운대");
        placeRequest.setMemberId(adminMemberId);
        placeRequest.setAddress("test-address");
        placeRequest.setDescription("test-description");
        placeRequest.setStartTime(LocalTime.of(8, 0));
        placeRequest.setEndTime(LocalTime.of(20, 0));
        placeRequest.setCategoryIds(List.of(childCategoryId));

        ResponseEntity<?> createResponse = placeService.savePlace(placeRequest);
        PlaceResponse createdPlace = (PlaceResponse) createResponse.getBody();
        assertNotNull(createdPlace);

        ResponseEntity<?> detailResponse = placeService.getPlaceDetails(createdPlace.getId());

        assertEquals(HttpStatus.OK, detailResponse.getStatusCode());
        PlaceResponse placeResponse = (PlaceResponse) detailResponse.getBody();
        assertNotNull(placeResponse);
        assertEquals("해운대", placeResponse.getName());
        assertEquals("test-description", placeResponse.getDescription());
    }

    @Test
    @DisplayName("여행지 삭제 테스트")
    void deletePlaceTest() throws AccessDeniedException {
        // 여행지 생성 요청
        PlaceRequest.placeSave placeRequest = new PlaceRequest.placeSave();
        placeRequest.setName("해운대");
        placeRequest.setMemberId(adminMemberId);
        placeRequest.setAddress("test-address");
        placeRequest.setDescription("test-description");
        placeRequest.setStartTime(LocalTime.of(8, 0));
        placeRequest.setEndTime(LocalTime.of(20, 0));
        placeRequest.setCategoryIds(List.of(childCategoryId));

        ResponseEntity<?> createResponse = placeService.savePlace(placeRequest);
        PlaceResponse createdPlace = (PlaceResponse) createResponse.getBody();
        assertNotNull(createdPlace);

        // 삭제 요청
        ResponseEntity<?> deleteResponse = placeService.deletePlace(createdPlace.getId(), adminMemberId);

        // 검증
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("성공적으로 여행지를 삭제 했습니다.", deleteResponse.getBody().toString());

        // 삭제된 여행지 조회 시도
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            placeService.getPlaceDetails(createdPlace.getId());
        });
    }

    @Test
    @DisplayName("필터링된 여행지 목록 페이징 조회 테스트")
    void getFilteredPlacesTest() throws AccessDeniedException {
        // 여행지 생성 요청
        PlaceRequest.placeSave placeRequest1 = new PlaceRequest.placeSave();
        placeRequest1.setName("해운대");
        placeRequest1.setMemberId(adminMemberId);
        placeRequest1.setAddress("test-address-1");
        placeRequest1.setDescription("test-description-1");
        placeRequest1.setStartTime(LocalTime.of(8, 0));
        placeRequest1.setEndTime(LocalTime.of(20, 0));
        placeRequest1.setCategoryIds(List.of(childCategoryId));
        placeService.savePlace(placeRequest1);

        PlaceRequest.placeSave placeRequest2 = new PlaceRequest.placeSave();
        placeRequest2.setName("광안리");
        placeRequest2.setMemberId(adminMemberId);
        placeRequest2.setAddress("test-address-2");
        placeRequest2.setDescription("test-description-2");
        placeRequest2.setStartTime(LocalTime.of(9, 0));
        placeRequest2.setEndTime(LocalTime.of(22, 0));
        placeRequest2.setCategoryIds(List.of(childCategoryId));
        placeService.savePlace(placeRequest2);

        // 필터링된 목록 요청
        Pageable pageable = PageRequest.of(0, 5); // 첫 번째 페이지, 페이지 크기 5
        Page<PlaceResponse> placeResponses = placeService.getAllPlaces(List.of(childCategoryId), pageable);

        // 검증
        assertNotNull(placeResponses);
        assertEquals(2, placeResponses.getContent().size());
        assertEquals("해운대", placeResponses.getContent().get(0).getName());
        assertEquals("광안리", placeResponses.getContent().get(1).getName());
    }

    @Test
    @DisplayName("여행지 수정 성공 테스트")
    void updatePlaceSuccessTest() throws AccessDeniedException {
        // 1. 여행지 생성
        PlaceRequest.placeSave placeRequest = new PlaceRequest.placeSave();
        placeRequest.setName("Original Name");
        placeRequest.setMemberId(adminMemberId);
        placeRequest.setAddress("Original Address");
        placeRequest.setDescription("Original Description");
        placeRequest.setCategoryIds(List.of(childCategoryId));
        placeRequest.setStrStartTime("08:00");
        placeRequest.setStrEndTime("20:00");
        ResponseEntity<?> createResponse = placeService.savePlace(placeRequest, null);
        PlaceResponse createdPlace = (PlaceResponse) createResponse.getBody();

        // 2. 수정 요청
        PlaceRequest.placeUpdate updateRequest = new PlaceRequest.placeUpdate();
        updateRequest.setMemberId(adminMemberId);
        updateRequest.setName("Updated Name");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStrStartTime("09:00");
        updateRequest.setStrEndTime("22:00");

        ResponseEntity<?> updateResponse = placeService.updatePlace(createdPlace.getId(), updateRequest, null);

        // 3. 검증
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        PlaceResponse updatedPlace = (PlaceResponse) updateResponse.getBody();
        assertEquals("Updated Name", updatedPlace.getName());
        assertEquals("Updated Description", updatedPlace.getDescription());
        assertEquals(LocalTime.of(9, 0), updatedPlace.getStartTime());
        assertEquals(LocalTime.of(22, 0), updatedPlace.getEndTime());
    }
}
