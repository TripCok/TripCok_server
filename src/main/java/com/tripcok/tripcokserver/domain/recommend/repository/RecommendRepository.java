package com.tripcok.tripcokserver.domain.recommend.repository;

import com.tripcok.tripcokserver.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends CrudRepository<Recommend,Long> {

    @Query("SELECT r, r.score FROM Recommend r ORDER BY r.score DESC")
    List<Recommend> findAllSortedByScoreDesc();

}
