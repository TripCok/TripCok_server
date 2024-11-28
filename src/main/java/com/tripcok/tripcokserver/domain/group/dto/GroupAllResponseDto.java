package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupAllResponseDto {
    private long id;
    private String groupName;
    private Integer groupMemberCount;
    private String description;
    private List<GroupCategoryResponse> category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean recruiting;

    // 생성자, Getter, Setter
    public GroupAllResponseDto(Group group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.groupMemberCount = group.getGroupMembers().size();
        this.description = group.getDescription();
        this.category = group.getCategory().stream().map(
                category -> new GroupCategoryResponse(category.getCategory())
        ).toList();
        this.createTime = group.getCreateTime();
        this.updateTime = group.getUpdateTime();
        this.recruiting = group.isRecruiting();
    }
}
