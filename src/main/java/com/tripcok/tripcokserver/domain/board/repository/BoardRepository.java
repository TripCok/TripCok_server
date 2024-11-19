package com.tripcok.tripcokserver.domain.board.repository;

import com.tripcok.tripcokserver.domain.board.entity.Board;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {
    // 특정 그룹의 게시글 조회
    List<Board> findByGroupId(Long groupId){
        return findByGroupId(groupId);
    };

    // 특정 사용자의 게시글 조회
    List<Board> findByUserId(Long userId){
        return findByUserId(userId);
    };

    // JPQL로 특정 조건 조회
    @Query("SELECT b FROM Board b WHERE b.group.id = :groupId AND b.user.id = :userId")
    List<Board> findByGroupAndUser(@Param("groupId") Long groupId, @Param("userId") Long userId) {

        return findByGroupAndUser(userId, groupId);
    }

    public Optional<Board> save(Board board) {
        return Optional.ofNullable(board);
    }
}
