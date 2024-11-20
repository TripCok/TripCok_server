package com.tripcok.tripcokserver.domain.group.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDto {

    /* 그룹 이름 */
    private String groupName;

    /* 그룹 소개 */
    private String description;

    /* 그룹 카테고리 */
    private String category;
}
