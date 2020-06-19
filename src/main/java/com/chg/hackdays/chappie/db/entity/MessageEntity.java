package com.chg.hackdays.chappie.db.entity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "message", schema = "public", catalog = "chappie")
public class MessageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "topic_offset")
    private long topicOffset;

    @Basic
    @Column(name = "text")
    private String text;

    @Basic
    @Column(name = "sent_time")
    private Instant sentTime;

    @Basic
    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "topic_id", insertable = false, updatable = false)
    long topicId;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    TopicEntity topic;

    @Column(name = "conversation_id", insertable = false, updatable = false)
    long conversationId;

    @ManyToOne
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    ConversationEntity conversation;

    @Column(name = "reply_to_id", insertable = false, updatable = false)
    Long replyToId;

    @ManyToOne
    @JoinColumn(name = "reply_to_id", referencedColumnName = "id")
    MessageEntity replyTo;

    @Column(name = "source_id", insertable = false, updatable = false)
    long sourceId;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    UserEntity source;

    @Column(name = "target_id", insertable = false, updatable = false)
    Long targetId;

    @ManyToOne
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    UserEntity target;

    @Column(name = "document_id", insertable = false, updatable = false)
    Long documentId;

    @ManyToOne
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    DocumentEntity document;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="id")
    @MapKeyColumn(name="name")
    private Map<String, MessageAttributeEntity> attributes = new HashMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTopicOffset() {
        return topicOffset;
    }

    public void setTopicOffset(long topicOffset) {
        this.topicOffset = topicOffset;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getSentTime() {
        return sentTime;
    }

    public void setSentTime(Instant sentTime) {
        this.sentTime = sentTime;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public TopicEntity getTopic() {
        return topic;
    }

    public void setTopic(TopicEntity topic) {
        this.topic = topic;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }

    public MessageEntity getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(MessageEntity replyTo) {
        this.replyTo = replyTo;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public UserEntity getSource() {
        return source;
    }

    public void setSource(UserEntity source) {
        this.source = source;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public UserEntity getTarget() {
        return target;
    }

    public void setTarget(UserEntity target) {
        this.target = target;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public void setDocument(DocumentEntity document) {
        this.document = document;
    }

    public Map<String, MessageAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, MessageAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public String getTopicName() {
        return getTopic().getName();
    }

    public String getSourceName() {
        if(getSource()==null)
            return null;
        return getSource().getName();
    }

    public String getTargetName() {
        if(getTarget()==null)
            return null;
        return getTarget().getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topicOffset, text, sentTime, createdTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return id == that.id &&
                topicOffset == that.topicOffset &&
                Objects.equals(text, that.text) &&
                Objects.equals(sentTime, that.sentTime) &&
                Objects.equals(createdTime, that.createdTime);
    }
}
