package com.tripcok.tripcokserver.domain.place.service;

import com.tripcok.tripcokserver.domain.file.FileDto;
import com.tripcok.tripcokserver.domain.file.service.FileService;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.domain.member.repository.MemberRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategoryMapping;
import com.tripcok.tripcokserver.domain.place.entity.PlaceImage;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryMappingRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceCategoryRepository;
import com.tripcok.tripcokserver.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileService fileService;

    @Value("${save.location.place}")
    private String savePathDirectory;

    /* 여행지 생성 */
    @Transactional(rollbackFor = {NoSuchElementException.class, AccessDeniedException.class})
    public ResponseEntity<?> savePlace(PlaceRequest.placeSave request, List<MultipartFile> files) throws AccessDeniedException {

        /* 1. 여행지 등록 사용자 권한 검사 */
        Member member = checkRole(request.getMemberId());

        /* 2. 여행지 엔티티 생성 및 저장 */
        Place place = new Place(request);
        Place newPlace = placeRepository.save(place);

        /* 2-1. 파일 저장 */
        if (files != null) {
            String savePath = System.getProperty("user.home") + savePathDirectory;

            List<FileDto> fileDtoList = fileService.saveFiles(files, savePath);
            for (FileDto fileDto : fileDtoList) {
                PlaceImage placeImage = new PlaceImage(fileDto.getName(), fileDto.getPath());
                newPlace.addImage(placeImage);
            }
        }


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

    /* 여행지 상세 조회 */
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPlaceDetails(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("여행지를 찾을 수 없습니다. ID: " + placeId));
        return ResponseEntity.status(HttpStatus.OK).body(new PlaceResponse(place, place.getCategoryMappings()));
    }

    /* 필터별 여행지 조회 */
    @Transactional(readOnly = true)
    public Page<PlaceResponse> getAllPlaces(List<Long> categoryIds, Pageable pageable) {
        Page<Place> places;

        if (categoryIds != null && !categoryIds.isEmpty()) {
            // 특정 카테고리 ID에 해당하는 여행지 조회
            places = placeRepository.findByCategoryIds(categoryIds, pageable);
        } else {
            // 모든 여행지 조회
            places = placeRepository.findAll(pageable);
        }

        // Place -> PlaceResponse 변환
        return places.map(place -> new PlaceResponse(place, place.getCategoryMappings()));
    }

    /* 여행지 업데이트 */
    @Transactional(rollbackFor = {NoSuchElementException.class, AccessDeniedException.class})
    public ResponseEntity<?> updatePlace(Long placeId, PlaceRequest.placeUpdate request, List<MultipartFile> files) throws AccessDeniedException {

        request.convertToLocalTime();

        /* 1. 권한 검사 */
        Member member = checkRole(request.getMemberId());

        /* 2. 여행지 조회 */
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("여행지를 찾을 수 없습니다. ID: " + placeId));

        /* 3. 요청 데이터로 필드 업데이트 */
        place.update(request);


        /* 4. 카테고리 매핑 업데이트 */
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<PlaceCategoryMapping> mappings = request.getCategoryIds().stream()
                    .map(categoryId -> {
                        PlaceCategory category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new NoSuchElementException("유효하지 않은 카테고리 ID: " + categoryId));
                        return new PlaceCategoryMapping(place, category);
                    })
                    .toList();

            // 기존 매핑 삭제 및 새 매핑 추가
            placeCategoryMappingRepository.deleteByPlaceId(placeId);
            placeCategoryMappingRepository.saveAll(mappings);
        }

        /* 5. 파일 처리 (기존 파일 유지 or 업데이트) */
        if (files != null) {
            String savePath = System.getProperty("user.home") + savePathDirectory;

            List<FileDto> fileDtoList = fileService.saveFiles(files, savePath);
            for (FileDto fileDto : fileDtoList) {
                PlaceImage placeImage = new PlaceImage(fileDto.getName(), fileDto.getPath());
                place.addImage(placeImage);
            }
        }

        /* 6. 업데이트된 데이터 응답 생성 */
        PlaceResponse response = new PlaceResponse(place, place.getCategoryMappings());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 여행지 삭제 */
    public ResponseEntity<?> deletePlace(Long placeId, Long memberId) throws AccessDeniedException {
        // 권한 검사
        checkRole(memberId);

        // 여행지 조회
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("여행지를 찾을 수 없습니다. ID: " + placeId));

        // 여행지 삭제
        placeRepository.delete(place);

        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 여행지를 삭제 했습니다.");
    }
}
