package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupCategory;
import com.tripcok.tripcokserver.domain.place.dto.PlaceCategoryResponse;
import com.tripcok.tripcokserver.domain.place.entity.PlaceCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
public class GroupResponseDto {

    /* 그룹 이름 */
    private String groupName;

    /* 그룹 소개 */
    private String description;

    /* 그룹 카테고리 */
    private List<GroupCategoryResponse> categories;

    private List<GroupMemberResponse> members;

    /* 그룹 구인 상태 */
    private boolean recruiting;

    public GroupResponseDto(Group group) {
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.categories = group.getCategory().stream().map(
                category -> new GroupCategoryResponse(category.getCategory())
        ).toList();
        this.members = group.getGroupMembers().stream().map(
                groupMember -> new GroupMemberResponse(groupMember.getMember())
        ).toList();

        this.recruiting = recruiting;
    }
}
