package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.group.entity.GroupCategory;
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

    /* 그룹 구인 상태 */
    private boolean recruiting;

    public GroupResponseDto(String groupName, String description, List<GroupCategory> categories, boolean recruiting) {
        this.groupName = groupName;
        this.description = description;
        this.categories = categories.stream().map(
                category -> new GroupCategoryResponse(category.getCategory())
        ).toList();
        this.recruiting = recruiting;
    }
}
