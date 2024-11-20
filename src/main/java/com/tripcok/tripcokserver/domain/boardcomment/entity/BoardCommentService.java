package com.tripcok.tripcokserver.domain.boardcomment.entity;

import com.tripcok.tripcokserver.domain.boardcomment.dto.BoardCommentResponseDto;
import com.tripcok.tripcokserver.domain.post.entity.PostRequestDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class BoardCommentService {
    /*댓글 작성*/
    public BoardCommentResponseDto createComment(Long userId, Long boardId, @Valid PostRequestDto requestDto) {
        //Member, board, post 생성
        //dto 로 Comment 생성
        //post.addcomment(comment)
        return null;
    }
}
