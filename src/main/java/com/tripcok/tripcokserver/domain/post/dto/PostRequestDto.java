package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Type;
import lombok.Data;


@Data
public class PostRequestDto {

    @Data
    public static class create {
        private String title;
        private String content;
        private Type type;
    }

    @Data
    public static class gets {
        private String title;
        private String content;
        private Type type;

        public gets(String title, String content, Type type) {
            this.title = title;
            this.content = content;
            this.type = type;
        }
    }

    @Data
    public static class put {
        private String Id;
        private String Title;
        private String Content;
        private Type type;
    }

    @Data
    public static class delete {
        private Long memberId;
        private Long groupId;
    }
}
