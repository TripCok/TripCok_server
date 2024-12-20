package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryResponse;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceCategoryService {

    private final PlaceCategoryRepository pcr;
    private final MemberRepository mr;

    /* 여행지 카테고리 생성 */
    /* 카테고리 생성 */
    @Transactional(rollbackOn = {IllegalArgumentException.class})
    public ResponseEntity<?> createCategory(PlaceCategoryRequest request) throws AccessDeniedException {

        checkRole(request.getMemberId());

        PlaceCategory category;

        // 부모 카테고리 검사
        PlaceCategory parent = request.getParentId() != null
                ? pcr.findById(request.getParentId()).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 부모 카테고리 ID: " + request.getParentId()))
                : null;

        // 중복 방지 검사: 자식들끼리 이름 중복 방지
        if (parent != null) {
            if (pcr.existsByNameAndParentCategoryId(request.getPlaceName(), parent.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(String.format("같은 부모 아래에서 이름이 중복됩니다. 부모 ID: %d, 이름: %s",
                                parent.getId(), request.getPlaceName()));
            }
        }
        // 부모 카테고리 설정
        category = new PlaceCategory(request);
        if (parent != null) {
            category.setParentCategory(parent); // 헬퍼 메서드 사용
        }

        PlaceCategory savedCategory = pcr.save(category);
        PlaceCategoryResponse response = new PlaceCategoryResponse(savedCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void checkRole(Long memberId) throws AccessDeniedException {

        Member findMember = mr.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("옳바르지 않은 객체로 접근 :" + memberId)
        );

        if (findMember.getRole().equals(Role.USER)) {
            throw new AccessDeniedException("옳바르지 않은 권한입니다. memberId :" + memberId);
        }

    }

    /* 모든 카테고리 조회 */
    public ResponseEntity<?> findByAllCategory() {

        List<PlaceCategory> topLevelCategories = pcr.findAllByParentCategoryIsNull();
        List<PlaceCategoryResponse> responses = topLevelCategories.stream()
                .map(PlaceCategoryResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }


    public ResponseEntity<?> findByCategory(Long parentId, String categoryName, Integer depth) {
        if (parentId == null) {
            List<PlaceCategory> byDepthAndName = pcr.findByDepthAndName(depth, categoryName);

            return ResponseEntity.status(HttpStatus.OK).body(byDepthAndName.stream()
                    .map(PlaceCategoryResponse::new).toList());
        }

        List<PlaceCategory> byCategory = pcr.findByParentCategoryIdAndNameAndDepth(parentId, categoryName, depth);

        return ResponseEntity.status(HttpStatus.OK).body(byCategory.stream()
                .map(PlaceCategoryResponse::new).toList());
    }
}
