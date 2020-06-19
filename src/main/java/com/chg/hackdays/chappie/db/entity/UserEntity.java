package com.chg.hackdays.chappie.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public", catalog = "chappie")
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToMany (mappedBy = "participants")
    private Set<ConversationEntity> conversations = new HashSet<>();

    public UserEntity() {
    }

    @PrePersist
    public void prePersist(){
        if(createdDate==null)
            createdDate=Instant.now();
    }

    public UserEntity(String name) {
        this.name=name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ConversationEntity> getConversations() {
        return conversations;
    }

    public void setConversations(Set<ConversationEntity> conversations) {
        this.conversations = conversations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(createdDate, that.createdDate);
    }
}
