package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupCategory;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryResponse;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
public class GroupResponseDto {

    private long id;
    private String groupName;
    private Integer groupMemberCount;
    private String description;
    private List<GroupCategoryResponse> categories;
    private List<GroupMemberResponse> members;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean recruiting;


    public GroupResponseDto(Group group, List<GroupCategory> categories, List<GroupMember> groupMembers) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.groupMemberCount = groupMembers.size();
        this.categories = categories.stream().map(
                category -> new GroupCategoryResponse(category.getCategory())
        ).toList();
        this.members = groupMembers.stream().map(
                groupMember -> new GroupMemberResponse(groupMember.getMember())
        ).toList();

        this.recruiting = group.isRecruiting();
    }

    public GroupResponseDto(Group group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.groupMemberCount = group.getGroupMembers().size();
        this.categories = group.getCategory().stream().map(
                category -> new GroupCategoryResponse(category.getCategory())
        ).toList();
        this.members = group.getGroupMembers().stream().map(
                groupMember -> new GroupMemberResponse(groupMember.getMember())
        ).toList();

        this.recruiting = group.isRecruiting();
    }
}
