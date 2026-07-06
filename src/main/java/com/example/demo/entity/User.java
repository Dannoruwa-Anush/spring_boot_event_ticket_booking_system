package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Data // getters & setters
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
