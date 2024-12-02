package com.tripcok.tripcokserver.global.controller;

import com.tripcok.tripcokserver.domain.group.dto.GroupAllResponseDto;
import com.tripcok.tripcokserver.domain.group.service.GroupService;
import com.tripcok.tripcokserver.domain.member.dto.MemberListResponseDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import com.tripcok.tripcokserver.domain.place.dto.PlaceRequest;
import com.tripcok.tripcokserver.domain.place.dto.PlaceResponse;
import com.tripcok.tripcokserver.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class WebAdminController {

    private final MemberService memberService;
    private final GroupService groupService;
    private final PlaceService placeService;
    private final HttpSession session;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/member")
    public String member(Model model,
                         @RequestParam(required = false) String query,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<MemberListResponseDto> members = memberService.getMembers(query, page, size);

        model.addAttribute("members", members);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", members.getTotalPages());
        model.addAttribute("member", session.getAttribute("member"));
        log.info(members.toString());

        return "member";
    }

    @GetMapping("/group")
    public String groups(Model model,
                         @RequestParam(required = false) List<Long> categoryIds,
                         @RequestParam(required = false) String query,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<?> groups = groupService.getGroups(categoryIds, query, page, size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());
        model.addAttribute("member", session.getAttribute("member"));
        log.info("Groups: {}", groups.getContent());
        return "group";
    }

    @GetMapping("/place")
    public String place(
            Model model,
            @RequestParam(required = false) String queryName,
            @RequestParam(required = false) List<Long> queryCategories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // PlaceResponse 리스트를 페이징 처리하여 가져옴
        Page<PlaceResponse> allPlaces = placeService.getAllPlaces(queryCategories, queryName, page, size);

        // 데이터 모델에 추가
        model.addAttribute("places", allPlaces); // 여행지 데이터
        model.addAttribute("currentPage", page);              // 현재 페이지
        model.addAttribute("totalPages", allPlaces.getTotalPages()); // 총 페이지
        model.addAttribute("totalElements", allPlaces.getTotalElements()); // 총 여행지 수
        model.addAttribute("member", session.getAttribute("member"));

        return "place";
    }

    @GetMapping("/place/add")
    public String addPlace(Model model) {
        model.addAttribute("request", new PlaceRequest.placeSave()); // 모델에 필요한 데이터 추가
        model.addAttribute("member", session.getAttribute("member"));
        return "addplace";
    }

    @GetMapping("/place/update")
    public String updatePlace(@RequestParam Long placeId, Model model) {
        ResponseEntity<?> placeResponse = placeService.getPlaceDetails(placeId);
        model.addAttribute("place", placeResponse.getBody());
        model.addAttribute("member", session.getAttribute("member"));
        model.addAttribute("placeId", placeId);
        return "updateplace";
    }

    @GetMapping("/category")
    public String category() {return "category";}
}


