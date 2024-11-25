package com.tripcok.tripcokserver.domain.postcomment.dto;

import com.tripcok.tripcokserver.domain.post.dto.PostResponseDto;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import lombok.Data;
import lombok.Setter;

@Data
public class PostCommentResponseDto {
    @Data
    public static class create{

    }

    @Data
    public static class get{
        private String content;

        public get(PostComment comment) {
            this.content = comment.getContent();
        }
    }

    @Data
    public static class gets {
        private String content;

        public gets(String content) {
            this.content = content;
        }
    }

    @Data
    public static class put {
        private Long id;
        private String message;

        public put(Long id, String message) {
            this.id = id;
            this.message = message;
        }
    }

    @Data
    public static class delete {

        Long postCommentId;
        String message;

        public delete(Long postCommentId, String s) {
            this.postCommentId = postCommentId;
            this.message = s;
        }
    }

    @Data
    public static class comment{
        private String message;
        private Long postId;

        public comment(String message, Long id) {
            this.message = message;
            this.postId = id;
        }
    }
}
