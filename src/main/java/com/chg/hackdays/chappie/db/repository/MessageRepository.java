package com.chg.hackdays.chappie.db.repository;

import com.chg.hackdays.chappie.db.entity.MessageEntity;
import com.chg.hackdays.chappie.model.MessageId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query("SELECT m FROM MessageEntity m WHERE topic.name = :topicName AND topicOffset >= :minOffset")
    List<MessageEntity> findByTopicNameAndMinOffset(String topicName, long minOffset);

    @Query("SELECT m FROM MessageEntity m WHERE topic.name = :topicName AND conversationId=:conversationId AND topicOffset >= :minOffset")
    List<MessageEntity> findByTopicNameAndConversationIdAndMinOffset(String topicName, Long conversationId, long minOffset);

    @Query("SELECT m FROM MessageEntity m WHERE topic.name = :topicName AND topicOffset = :topicOffset")
    MessageEntity findOneByTopicNameAndTopicOffset(String topicName, long topicOffset);

    @Query("SELECT m FROM MessageEntity m WHERE topic.name = :topicName AND topicOffset >= :minOffset")
    List<MessageEntity> findByTopicNameAndMinOffset(String topicName, long minOffset, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE topic.name = :topicName AND conversationId=:conversationId AND topicOffset >= :minOffset")
    List<MessageEntity> findByTopicNameAndConversationIdAndMinOffset(String topicName, Long conversationId, long minOffset, Pageable pageable);

    default MessageEntity findOneByMessageId(MessageId messageId) {
        return findOneByTopicNameAndTopicOffset(messageId.getTopic(), messageId.getOffset());
    }

    @Query("SELECT MAX(topicOffset) FROM MessageEntity m WHERE topic.name = :topicName")
    Long findMaxOffsetByTopicName(String topicName);

    @Query("SELECT MAX(topicOffset) FROM MessageEntity m WHERE topic.name = :topicName AND conversationId=:conversationId")
    Long findMaxOffsetByTopicNameAndConversationId(String topicName, Long conversationId);
}
