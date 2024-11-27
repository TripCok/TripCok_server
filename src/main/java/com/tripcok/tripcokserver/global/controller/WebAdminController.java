package com.tripcok.tripcokserver.global.controller;

import com.tripcok.tripcokserver.domain.group.dto.GroupAllResponseDto;
import com.tripcok.tripcokserver.domain.group.service.GroupService;
import com.tripcok.tripcokserver.domain.member.dto.MemberListResponseDto;
import com.tripcok.tripcokserver.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class WebAdminController {

    private final MemberService memberService;
    private final GroupService groupService;

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
        log.info(members.toString());

        return "member";
    }

    @GetMapping("/group")
    public String groups(Model model,
                         @RequestParam(required = false) String query,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<GroupAllResponseDto> groups = groupService.getGroups(query, page, size);

        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());
        log.info("Groups: {}", groups.getContent());
        return "group";
    }

}



