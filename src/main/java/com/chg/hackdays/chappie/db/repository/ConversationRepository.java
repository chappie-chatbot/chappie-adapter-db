package com.chg.hackdays.chappie.db.repository;

import com.chg.hackdays.chappie.db.entity.ConversationEntity;
import com.chg.hackdays.chappie.db.entity.MessageEntity;
import com.chg.hackdays.chappie.db.entity.TopicEntity;
import com.chg.hackdays.chappie.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    Collection<ConversationEntity> findByParticipants(UserEntity participant);
}
