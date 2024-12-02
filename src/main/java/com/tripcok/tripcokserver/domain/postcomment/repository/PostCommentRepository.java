package com.tripcok.tripcokserver.domain.postcomment.repository;

import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findByPostId(Long postId, Pageable pageable);
}
