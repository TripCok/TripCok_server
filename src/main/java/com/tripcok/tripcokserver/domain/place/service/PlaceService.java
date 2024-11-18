package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryMappingRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final PlaceCategoryRepository categoryRepository;
    private final PlaceCategoryMappingRepository placeCategoryMappingRepository;

    @Value("${save.location.place-thumbnail}")
    private String savePath;

    /* 여행지 생성 */
    @Transactional(rollbackFor = {NoSuchElementException.class, AccessDeniedException.class})
    public ResponseEntity<?> savePlace(PlaceRequest.save request) throws AccessDeniedException {

        /* 1. 여행지 등록 사용자 권한 검사 */
        Member member = checkRole(request.getMemberId());

        /* 2. 여행지 엔티티 생성 및 저장 */
        Place place = new Place(request);
        Place newPlace = placeRepository.save(place);

        /* 3. 카테고리 매핑 생성 */
        List<PlaceCategoryMapping> mappings = request.getCategoryIds().stream()
                .map(categoryId -> {
                    PlaceCategory category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NoSuchElementException("유효하지 않은 카테고리 ID: " + categoryId));
                    return new PlaceCategoryMapping(newPlace, category);
                })
                .toList();

        placeCategoryMappingRepository.saveAll(mappings);

        /* 4. 응답 데이터 생성 */
        PlaceResponse response = new PlaceResponse(newPlace, mappings);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /* 여행지 등록 사용자 권환 검사 */
    private Member checkRole(Long memberId) throws AccessDeniedException {

        /* 사용자 정보 조회 */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자의 정보가 옳바르지 않습니다. Member ID : " + memberId));


        if (!member.getRole().equals(Role.MANAGER)) {
            throw new AccessDeniedException("올바르지 않은 권한입니다. Member ID: " + memberId);
        }

        return member;
    }
}
