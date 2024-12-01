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
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final PlaceRepository placeRepository;
    private final GroupRepository groupRepository;

    public List<RecommendResponseDto> getRecommends() {
           List<Recommend> recommends = recommendRepository.findAllSortedByScoreDesc();
           List<RecommendResponseDto> responseDtos = new ArrayList<>();
           recommends.forEach(recommend -> {
                Place place = recommend.getPlace();
                PlaceResponse placeResponse = new PlaceResponse(place, place.getCategoryMappings());
                responseDtos.add(new RecommendResponseDto(recommend.getScore(), recommend.getGroup().getId(), placeResponse));
           });
           return responseDtos;
    }

    public RecommendResponseDto putRecommend(RecommendRequestDto requestDto) {

        Place place = placeRepository.findById(requestDto.getPlace_id()).orElseThrow();
        Group group = groupRepository.findById(requestDto.getGroup_id()).orElseThrow();

        Recommend recommend = new Recommend(requestDto,group,place);

        recommendRepository.save(recommend);
        PlaceResponse placeResponseDto = new PlaceResponse(place,place.getCategoryMappings());
        RecommendResponseDto responseDto = new RecommendResponseDto(requestDto.getScore(), requestDto.getGroup_id(), placeResponseDto);
        return responseDto;
    }
}
