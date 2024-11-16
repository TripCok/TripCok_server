package com.tripcok.tripcokserver.domain.group.dto;

import com.tripcok.tripcokserver.domain.board.entity.Board;
import com.tripcok.tripcokserver.domain.group.entity.Group;
import com.tripcok.tripcokserver.domain.group.entity.GroupMember;
import com.tripcok.tripcokserver.domain.group.entity.GroupRole;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupResponseDto {

    /* 모임 조회 - 단일 */
    @Data
    public static class Info {
        private Long id;
        private String groupName;
        private String description;
        private String category;
        private List<GroupMember> groupMembers; // 멤버 리스트
        private List<Board> boardlist; // 게시판 리스트

        public Info(Group group) {
            this.groupName = group.getGroupName();
            this.groupMembers = group.getGroupMembers();
            this.boardlist = group.getBoardlist();
            this.description = group.getDescription();
            this.category = group.getCategory();
        }
    }

    /* 멤버 정보 */
    @Data
    public static class MemberInfo {
        private Long id;
        private String name;
        private String email;
        private GroupRole role;

        public MemberInfo(Member member, GroupRole role) {
            this.id = member.getId();
            this.name = member.getName();
            this.role = role;
        }
    }


    /* 게시판 정보 */
    @Data
    public static class BoardInfo {
        private Long id;
        private String title;
        private String content;

        public BoardInfo(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }

    }

}
