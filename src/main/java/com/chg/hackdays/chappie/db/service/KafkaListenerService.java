package com.chg.hackdays.chappie.db.service;

import com.chg.hackdays.chappie.db.entity.ConversationEntity;
import com.chg.hackdays.chappie.db.entity.DocumentEntity;
import com.chg.hackdays.chappie.db.entity.MessageAttributeEntity;
import com.chg.hackdays.chappie.db.entity.MessageEntity;
import com.chg.hackdays.chappie.db.entity.TopicEntity;
import com.chg.hackdays.chappie.db.entity.UserEntity;
import com.chg.hackdays.chappie.db.repository.ConversationRepository;
import com.chg.hackdays.chappie.db.repository.DocumentRepository;
import com.chg.hackdays.chappie.db.repository.MessageAttributeRepository;
import com.chg.hackdays.chappie.db.repository.MessageRepository;
import com.chg.hackdays.chappie.db.repository.TopicRepository;
import com.chg.hackdays.chappie.db.repository.UserRepository;
import com.chg.hackdays.chappie.model.Message;
import com.chg.hackdays.chappie.model.MessageId;
import com.chg.hackdays.chappie.util.EncodeUtil;
import com.chg.hackdays.chappie.util.MimeUtil;
import com.chg.hackdays.chappie.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaListenerService {
    private static final String TOPIC_CHAT = "chat";
    private static final String TOPIC_DOCUMENT = "document";
    private static final String KAFKA_GROUP_ID = "chappie-db-adapter";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    MessageAttributeRepository messageAttributeRepository;

    @KafkaListener(topics = TOPIC_CHAT, groupId = KAFKA_GROUP_ID)
    public void listenChat(GenericMessage<Message> genericMessage, @Payload Message message) {
        listen(genericMessage, message);
    }

    @KafkaListener(topics = TOPIC_DOCUMENT, groupId = KAFKA_GROUP_ID)
    public void listenDocument(GenericMessage<Message> genericMessage, @Payload Message message) {
        listen(genericMessage, message);
    }

    public void listen(GenericMessage<Message> genericMessage, @Payload Message message) {
        try {
            message.setId(new MessageId(message.getTopic(), (Long) genericMessage.getHeaders().get("kafka_offset")));
            log.debug("Retrieved message: {}", message.getId());
            saveMessage(message);
        } catch (Exception e) {
            log.error("Failed to process chat message", e);
            throw new RuntimeException(e);
        }
    }

    // TODO: Use ModelMapper
    private MessageEntity saveMessage(Message message) {
        MessageId messageId = new MessageId(message.getId());
        MessageEntity entity = messageRepository.findOneByMessageId(messageId);
        if (entity != null) {
            log.warn("Message already exists");
        } else {
            entity = new MessageEntity();

            TopicEntity topic = getOrCreateTopic(messageId.getTopic());
            ConversationEntity conversation = getOrCreateConversation(message.getConversation());

            entity.setTopic(topic);
            entity.setTopicOffset(messageId.getOffset());
            entity.setConversation(conversation);
            if (message.getReplyTo() != null) {
                entity.setReplyTo(messageRepository.findOneByMessageId(new MessageId(message.getReplyTo())));
            }
            UserEntity source = getOrCreateUser(message.getSource());
            if (source != null) {
                entity.setSource(source);
                // Attach source user as participant of conversation
                conversation.getParticipants().add(source);
            }
            UserEntity target = getOrCreateUser(message.getTarget());
            if (target != null) {
                entity.setTarget(target);
                // Attach target user as participant of conversation
                conversation.getParticipants().add(target);
            }
            // Save participants added to conversation
            conversationRepository.save(conversation);

            if (MimeUtil.isText(message.getMime())) {
                entity.setText(message.getText());
            } else {
                byte[] data = EncodeUtil.decode(message.getType(), message.getText());
                entity.setDocument(getOrCreateDocument(data, message.getMime()));
            }
            entity.setSentTime(message.getTimestamp().toInstant());

            // Persist the message
            messageRepository.save(entity);

            // Save attributes associated with message
            if (!message.getAttributes().isEmpty()) {
                Map<String, MessageAttributeEntity> attributes = new HashMap<>();
                for (Map.Entry<String, String> e : message.getAttributes().entrySet()) {
                    MessageAttributeEntity attribute = new MessageAttributeEntity(entity.getId(), e.getKey(), e.getValue());
                    attributes.put(attribute.getName(), attribute);
                }
                entity.setAttributes(attributes);
                messageAttributeRepository.saveAll(attributes.values());
            }
        }
        return entity;
    }

    private DocumentEntity getOrCreateDocument(byte[] data, String mime) {
        byte[] hash = EncodeUtil.hash(data);
        DocumentEntity document = documentRepository.findOneByLengthAndHash(data.length, hash);
        if (document == null) {
            document = new DocumentEntity(hash, data, mime);
            documentRepository.save(document);
        }
        return document;
    }

    private ConversationEntity getOrCreateConversation(Long conversationId) {
        ConversationEntity conversation = null;
        if (conversationId != null)
            conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            conversation = new ConversationEntity(conversationId);
            conversationRepository.save(conversation);
        }
        return conversation;
    }

    private TopicEntity getOrCreateTopic(String topicName) {
        TopicEntity topic = topicRepository.findOneByName(topicName);
        if (topic == null) {
            topic = new TopicEntity(topicName);
            topicRepository.save(topic);
        }
        return topic;
    }

    private UserEntity getOrCreateUser(String userName) {
        if(StringUtil.isBlank(userName))
            return null;
        UserEntity user = userRepository.findOneByName(userName);
        if (user == null) {
            user = new UserEntity(userName);
            userRepository.save(user);
        }
        return user;
    }
}
