package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import lombok.Data;

@Data
public class GroupAllResponseDto {
    private long id;
    private String groupName;
    private String description;
    private String category;
    private boolean recruiting;

    // 생성자, Getter, Setter
    public GroupAllResponseDto(Group group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.category = group.getCategory();
        this.recruiting = group.getRecruiting();
    }
}
