package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Type;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;

@Data
public class PostRequestDto {

    @Data
    public static class create{
        private Long id;
        private String title;
        private String content;
        private Type type;
    }

    @Data
    public static class comment{

    }

    @Data
    public static class get {
        private Long id;
        private String title;
        private String content;
        private Type type;
    }

    @Data
    public static class gets {
        private Long id;
        private String title;
        private String content;
        private Type type;

        public gets(Long id, String title, String content, Type type) {
            this.id = id;
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
