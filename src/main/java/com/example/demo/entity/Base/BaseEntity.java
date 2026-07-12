package com.example.demo.entity.Base;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    protected String createdBy;

    @Column(name = "updated_by")
    protected String updatedBy;
    
    @PrePersist // is called before the entity is inserted
    public void onPrePersist() {
        ZonedDateTime zonedNowUtc = ZonedDateTime.now(ZoneId.of("UTC")); // Convert to UTC time zone
        this.createdAt = zonedNowUtc.toLocalDateTime(); // Convert to LocalDateTime for storage
        this.updatedAt = zonedNowUtc.toLocalDateTime(); // Convert to LocalDateTime for storage
    }

    @PreUpdate // is called before the entity is updated
    public void onPreUpdate() {
        ZonedDateTime zonedNowUtc = ZonedDateTime.now(ZoneId.of("UTC"));// Convert to UTC time zone
        this.updatedAt = zonedNowUtc.toLocalDateTime(); // Convert to LocalDateTime for storage
    }
}
