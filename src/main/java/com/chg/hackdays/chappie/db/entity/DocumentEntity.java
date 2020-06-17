package com.chg.hackdays.chappie.db.entity;

import com.chg.hackdays.chappie.util.EncodeUtil;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "document", schema = "public", catalog = "chappie")
public class DocumentEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "length", insertable = false, updatable = false)
    private long length;

    @Basic
    @Column(name = "hash", insertable = false, updatable = false)
    private byte[] hash;

    @Basic
    @Column(name = "data")
    private byte[] data;

    @Basic
    @Column(name = "mime")
    private String mime;

    public DocumentEntity() {
    }

    public DocumentEntity(byte[] data, String mime) {
        this.length = data.length;
        this.hash = EncodeUtil.hash(data);
        this.data = data;
        this.mime = mime;
    }

    public DocumentEntity(byte[] hash, byte[] data, String mime) {
        this.length = data.length;
        this.hash = hash;
        this.data = data;
        this.mime = mime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String type) {
        this.mime = type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, length, mime);
        result = 31 * result + Arrays.hashCode(hash);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return id == that.id &&
                length == that.length &&
                Arrays.equals(hash, that.hash) &&
                Arrays.equals(data, that.data) &&
                Objects.equals(mime, that.mime);
    }
}
