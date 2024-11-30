package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.domain.post.entity.Type;
import lombok.Data;


@Data
public class PostResponseDto {

    @Data
    public static class create{
        private String message;
        private Long postId;
        private Type type;

        public create(String message, Long id, Type type) {
            this.message = message;
            this.postId = id;
            this.type = type;
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

    @Data
    public static class get {
        private Long id;
        private String title;
        private String content;
        private Type type;

        public get(Post post){
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.type = post.getType();
        }
    }

    @Data
    public static class gets {
        private String title;
        private String content;
        private Type type;

        public gets( String title, String content  , Type type  ) {
            this.title = title;
            this.content = content;
            this.type = type;
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
        private Long postId;
        private String message;

        public delete(Long postId, String message) {
            this.postId = postId;
            this.message = message;
        }
    }


}
