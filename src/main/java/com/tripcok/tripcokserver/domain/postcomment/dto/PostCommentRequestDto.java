package com.tripcok.tripcokserver.domain.postcomment.dto;

import lombok.Data;

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
    }

    @Data
    public static class delete {
        private Long memberId;
        private Long groupId;
    }
}
