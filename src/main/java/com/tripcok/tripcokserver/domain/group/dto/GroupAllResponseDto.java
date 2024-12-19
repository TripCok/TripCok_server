package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupAllResponseDto {
    private long id;
    private String groupName;
    private Integer groupMemberCount;
    private long groupOwnerId;
    private String description;
    private List<GroupCategoryResponse> categories;
    private List<GroupMemberResponse> members;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean recruiting;

    // 생성자, Getter, Setter
    public GroupAllResponseDto(Group group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.groupMemberCount = group.getGroupMembers().size();
        this.description = group.getDescription();
        this.groupOwnerId = getOwner(group.getGroupMembers());
        this.categories = group.getCategory().stream().map(
                category -> new GroupCategoryResponse(category.getCategory())
        ).toList();
        this.members = group.getGroupMembers().stream().map(
                groupMember -> new GroupMemberResponse(groupMember.getMember())
        ).toList();
        this.createTime = group.getCreateTime();
        this.updateTime = group.getUpdateTime();
        this.recruiting = group.isRecruiting();
    }

    private long getOwner(List<GroupMember> groupMembers) {
        for (GroupMember groupMember : groupMembers) {
            if (groupMember.getRole().equals(GroupRole.ADMIN)) {
                return groupMember.getMember().getId();
            }
        }
        return 0;
    }
}
