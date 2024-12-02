package com.tripcok.tripcokserver.domain.recommend.service;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.repository.GroupRepository;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.entity.Place;
import com.tripcok.tripcokserver.domain.place.repository.PlaceRepository;
import com.tripcok.tripcokserver.domain.recommend.dto.RecommendRequestDto;
import com.tripcok.tripcokserver.domain.recommend.dto.RecommendResponseDto;
import com.tripcok.tripcokserver.domain.recommend.entity.Recommend;
import com.tripcok.tripcokserver.domain.recommend.repository.RecommendRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final PlaceRepository placeRepository;
    private final GroupRepository groupRepository;

    public Page<PlaceResponse> getRecommendPlaces(int page, int size) {
        // 추천 장소 리스트 생성
        List<PlaceResponse> recommendPlaces = recommendRepository.findAllSortedByScoreDesc()
                .stream()
                .map(recommend -> new PlaceResponse(recommend.getPlace(), recommend.getPlace().getCategoryMappings()))
                .toList();

        // 일반 장소 리스트 생성
        List<PlaceResponse> restPlaces = placeRepository.findAllExceptRecommends()
                .stream()
                .map(restPlace -> new PlaceResponse(restPlace, restPlace.getCategoryMappings()))
                .toList();

        // 추천 장소와 일반 장소 합치기
        List<PlaceResponse> allPlaces = new ArrayList<>(recommendPlaces);
        allPlaces.addAll(restPlaces);

        PageRequest pageRequest = PageRequest.of(page, size);

        // Page 객체 생성 및 반환
        Page<PlaceResponse> pageResult = new PageImpl<>(
                allPlaces.subList(
                        Math.min(page * size, allPlaces.size()),
                        Math.min((page + 1) * size, allPlaces.size())
                ),
                pageRequest,
                allPlaces.size()
        );

        return pageResult;
    }

    public RecommendResponseDto putRecommend(RecommendRequestDto requestDto) {

        Place place = placeRepository.findById(requestDto.getPlace_id()).orElseThrow();
        Group group = groupRepository.findById(requestDto.getGroup_id()).orElseThrow();

        Recommend recommend = new Recommend(requestDto,group,place);

        recommendRepository.save(recommend);
        PlaceResponse placeResponseDto = new PlaceResponse(place,place.getCategoryMappings());
        return new RecommendResponseDto(requestDto.getScore(), requestDto.getGroup_id(), placeResponseDto);
    }
}
