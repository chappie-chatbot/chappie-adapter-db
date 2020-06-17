package com.chg.hackdays.chappie.db.repository;

import com.chg.hackdays.chappie.db.entity.MessageAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageAttributeRepository extends JpaRepository<MessageAttributeEntity, MessageAttributeEntity.MessageAttributeId> {
}
