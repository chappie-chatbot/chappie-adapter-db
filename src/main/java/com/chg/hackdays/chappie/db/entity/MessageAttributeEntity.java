package com.chg.hackdays.chappie.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "message_attribute", schema = "public", catalog = "chappie")
@IdClass(MessageAttributeEntity.MessageAttributeId.class)
public class MessageAttributeEntity {
    @Id
    long messageId;

    @Id
    String name;

    @Column(name="value")
    String value;

    public MessageAttributeEntity() {
    }

    public MessageAttributeEntity(long messageId, String name, String value) {
        this.messageId = messageId;
        this.name = name;
        this.value = value;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static class MessageAttributeId implements Serializable {
        @Column(name="message_id")
        long messageId;

        @Column(name="name")
        String name;

        @Override
        public int hashCode() {
            return Objects.hash(messageId, name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MessageAttributeId that = (MessageAttributeId) o;
            return messageId == that.messageId &&
                    Objects.equals(name, that.name);
        }
    }
}
