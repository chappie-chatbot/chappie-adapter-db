package com.chg.hackdays.chappie.db.repository;

import com.chg.hackdays.chappie.db.entity.MessageEntity;
import com.chg.hackdays.chappie.db.entity.TopicEntity;
import com.chg.hackdays.chappie.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findOneByName(String name);
}
