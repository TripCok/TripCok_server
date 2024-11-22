package com.tripcok.tripcokserver.domain.post.dto;

import com.tripcok.tripcokserver.domain.post.entity.Post;
import com.tripcok.tripcokserver.domain.post.entity.Type;
import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostPageResponseDto {

    private Long id;
    private String title;
    private String content;
    private Type type;

    public PostPageResponseDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
