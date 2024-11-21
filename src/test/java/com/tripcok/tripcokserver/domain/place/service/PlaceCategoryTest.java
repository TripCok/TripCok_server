package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.member.dto.MemberRequestDto;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryResponse;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@SpringBootTest
@Transactional
public class PlaceCategoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlaceCategoryService placeCategoryService;

    @Autowired
    private PlaceCategoryRepository placeCategoryRepository;

    @BeforeEach
    public void setUp() {

        /* 사용자 생성 - 1 */
        MemberRequestDto.save newMember = new MemberRequestDto.save();
        newMember.setName("test");
        newMember.setEmail("test@tripcok.com");
        newMember.setPassword("123456");

        Member member = new Member(newMember);
        member.setRole(Role.MANAGER);
        memberRepository.save(member);

        /* 사용자 생성 - 2 */
        MemberRequestDto.save newMember2 = new MemberRequestDto.save();
        newMember2.setName("test");
        newMember2.setEmail("test@tripcok.com");
        newMember2.setPassword("123456");

        Member member2 = new Member(newMember2);
        memberRepository.save(member2);

    }

    @Test
    @Rollback(false)
    @DisplayName("부모 카테고리 생성 실패 테스트")
    public void createCategoryFailTest() throws AccessDeniedException {

        /**
         *  2번 사용자는 카테고리 생성이 불가능하다.
         *  2번 사용자가 Category를 생성하려 하면 AccessDeniedException이 발생해야한다.
         *  */

        PlaceCategoryRequest newCategory = new PlaceCategoryRequest();
        newCategory.setPlaceName("test");
        newCategory.setMemberId(2L);

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            placeCategoryService.createCategory(newCategory);
        });

    }

    @Test
    @Rollback(false)
    @DisplayName("부모 카테고리 생성 성공 테스트")
    public void createCategorySuccessTest() throws AccessDeniedException {

        /* # 1번 사용자는 카테고리 생성을 성공해야한다. */
        PlaceCategoryRequest newCategory = new PlaceCategoryRequest();
        newCategory.setPlaceName("test");
        newCategory.setMemberId(1L);

        placeCategoryService.createCategory(newCategory);

        PlaceCategory placeCategory = placeCategoryRepository.findById(1L).get();

        /* # 생성된 값과 데이터베이스에서 검색된 값이 일치해야한다. */
        Assertions.assertEquals(newCategory.getPlaceName(), placeCategory.getName());

    }

    @Test
    @Rollback(false)
    @DisplayName("부모 카테고리와 자식 카테고리 테스트")
    public void createCategoryChildTest() throws AccessDeniedException {
        /* 1번 카테고리 */
        PlaceCategoryRequest newCategory = new PlaceCategoryRequest();
        newCategory.setPlaceName("test");
        newCategory.setMemberId(1L);

        placeCategoryService.createCategory(newCategory);

        PlaceCategory firstCategory = placeCategoryRepository.findById(1L).get();

        /* 2번 카테고리 */
        PlaceCategoryRequest newCategory2 = new PlaceCategoryRequest();
        newCategory2.setMemberId(1L);
        newCategory2.setPlaceName("test2");
        newCategory2.setParentId(firstCategory.getId());

        placeCategoryService.createCategory(newCategory2);

        PlaceCategory secondCategory = placeCategoryRepository.findById(2L).get();

        /* #자식 카테고리와 부모 카테고리는 같아야한다. */
        Assertions.assertEquals(secondCategory.getParentCategory().getId(), firstCategory.getId());

    }

    @Test
    @Rollback(false)
    @DisplayName("모든 카테고리 조회 테스트")
    public void findAllCategoriesTest() throws AccessDeniedException {

        // 부모 카테고리
        PlaceCategoryRequest parentCategoryRequest = new PlaceCategoryRequest();
        parentCategoryRequest.setPlaceName("Parent Category");
        parentCategoryRequest.setMemberId(1L);
        placeCategoryService.createCategory(parentCategoryRequest);

        // 자식 카테고리 1
        PlaceCategory parentCategory = placeCategoryRepository.findAll().stream()
                .filter(category -> "Parent Category".equals(category.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("부모 카테고리가 생성되지 않았습니다."));

        PlaceCategoryRequest childCategoryRequest1 = new PlaceCategoryRequest();
        childCategoryRequest1.setPlaceName("Child Category 1");
        childCategoryRequest1.setParentId(parentCategory.getId());
        childCategoryRequest1.setMemberId(1L);
        placeCategoryService.createCategory(childCategoryRequest1);

        // 자식 카테고리 2
        PlaceCategoryRequest childCategoryRequest2 = new PlaceCategoryRequest();
        childCategoryRequest2.setPlaceName("Child Category 2");
        childCategoryRequest2.setParentId(parentCategory.getId());
        childCategoryRequest2.setMemberId(1L);
        placeCategoryService.createCategory(childCategoryRequest2);

        // When: 모든 카테고리 조회
        ResponseEntity<?> response = placeCategoryService.findByAllCategory();
        List<PlaceCategoryResponse> categoryResponses = (List<PlaceCategoryResponse>) response.getBody();

        // Then: 반환된 데이터 검증
        Assertions.assertNotNull(categoryResponses, "카테고리 응답이 null입니다.");
        Assertions.assertFalse(categoryResponses.isEmpty(), "카테고리가 비어 있습니다.");

        PlaceCategoryResponse parentResponse = categoryResponses.stream()
                .filter(category -> "Parent Category".equals(category.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("반환된 데이터에 부모 카테고리가 없습니다."));

        Assertions.assertEquals(2, parentResponse.getChildren().size(), "부모 카테고리의 자식 개수가 일치하지 않습니다.");

        List<String> childNames = parentResponse.getChildren().stream()
                .map(PlaceCategoryResponse::getName)
                .toList();

        Assertions.assertTrue(childNames.contains("Child Category 1"), "자식 카테고리 1이 누락되었습니다.");
        Assertions.assertTrue(childNames.contains("Child Category 2"), "자식 카테고리 2가 누락되었습니다.");
    }


}
