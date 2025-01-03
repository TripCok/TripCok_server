package com.tripcok.tripcokserver.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequestDto {

    /* 사용자 아이디 */
    private Long memberId;

    /* 그룹 이름 */
    private String groupName;

    /* 설명 */
    private String description;

    /* 카테고리 */
    private List<Long> categories;

    @Data
    public static class update{
        private Long memberId;
        private String groupName;
        private String description;
    }

}