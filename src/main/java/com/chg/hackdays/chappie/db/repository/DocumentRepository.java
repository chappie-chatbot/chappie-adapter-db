package com.chg.hackdays.chappie.db.repository;

import com.chg.hackdays.chappie.db.entity.DocumentEntity;
import com.chg.hackdays.chappie.db.entity.MessageEntity;
import com.chg.hackdays.chappie.db.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    DocumentEntity findOneByLengthAndHash(long length, byte[] hash);
}
