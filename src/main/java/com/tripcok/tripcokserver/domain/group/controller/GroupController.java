package com.tripcok.tripcokserver.domain.group.controller;

import com.tripcok.tripcokserver.domain.group.dto.GroupResponseDto;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/") // 수정해야함
@RequiredArgsConstructor

public class GroupController {
    private final GroupService groupService;

    /* 그룹 생성 */
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupResponseDto.Info request) {
        return groupService.createGroup(request);
    }

}
