package com.tripcok.tripcokserver.domain.group.repository;

import com.tripcok.tripcokserver.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
