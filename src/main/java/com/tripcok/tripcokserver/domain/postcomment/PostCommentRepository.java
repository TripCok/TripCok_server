package com.tripcok.tripcokserver.domain.postcomment;

import com.tripcok.tripcokserver.domain.postcomment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
