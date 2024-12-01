package com.tripcok.tripcokserver.global.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "create_time", updatable = false, nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public LocalDateTime getCreateTime() {
        return createAt;
    }

    public LocalDateTime getUpdateTime() {
        return updateAt;
    }
}