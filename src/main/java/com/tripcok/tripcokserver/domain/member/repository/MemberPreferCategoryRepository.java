package com.tripcok.tripcokserver.domain.member.repository;

import com.tripcok.tripcokserver.domain.member.entity.MemberPreferenceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPreferCategoryRepository extends JpaRepository<MemberPreferenceCategory, Long> {

}
