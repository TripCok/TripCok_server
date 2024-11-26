package com.tripcok.tripcokserver.domain.postcomment.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class PostCommentRequestDto {

    @Data
    public static class create{
        private String content;
    }

    @Data
    public static class comment{
        private String content;
        private Long postId;
        private Long memberId;
        private Long groupId;
    }

    @Data
    public static class gets {
        private String content;

        public gets( String content ) {
            this.content = content;
        }
    }

    @Data
    public static class put {
        private String Content;
        private Long postCommentId;
        private Long memberId;
    }

    @Data
    public static class delete {
        private Long postCommentId;
        private Long memberId;
    }
}
