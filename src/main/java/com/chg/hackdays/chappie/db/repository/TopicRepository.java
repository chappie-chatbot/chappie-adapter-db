package com.chg.hackdays.chappie.db.repository;

import com.chg.hackdays.chappie.db.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    TopicEntity findOneByName(String name);
}
