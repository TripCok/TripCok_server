package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryMappingRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public ResponseEntity<?> savePlace(PlaceRequest.save request) throws AccessDeniedException {

        /* 여행지 등록 사용자 권환 검사 */
        Member member = checkRole(request.getMemberId());

        /*여행지 Entity 생성 */
        Place place = new Place(request);

        Place newPlace = placeRepository.save(place);

        /* 여행지에 대한 카테고리 추가 */
        for (Long categoryId : request.getCategoryIds()) {

            Optional<PlaceCategory> byId = categoryRepository.findById(categoryId);

            /* 추후 개선 방안 생각 해봐야함*/
            if (byId.isEmpty()) {
                continue;
            } else {
                PlaceCategory category = byId.get();
                PlaceCategoryMapping pcm = new PlaceCategoryMapping(newPlace, category);

                placeCategoryMappingRepository.save(pcm);
            }
        }
        return ResponseEntity.ok(placeRepository.findById(newPlace.getId()));
    }

    /* 여행지 등록 사용자 권환 검사 */
    private Member checkRole(Long memberId) throws AccessDeniedException {

        /* 사용자 정보 조회 */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("사용자의 정보가 옳바르지 않습니다. Member ID : " + memberId));


        if (!member.getRole().equals(Role.MANAGER)) {
            throw new AccessDeniedException("올바르지 않은 권한입니다. Member ID: " + memberId);
        }

        return member;

    }
}
