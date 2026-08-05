package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.entity.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Data // getters & setters
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    // Security Management
    @Column(nullable = false)
    private boolean mustChangePassword;

    private LocalDateTime passwordChangedAt;

    @Column(nullable = true)
    private int failedLoginAttempts;

    @Column(nullable = true)
    private LocalDateTime accountLockedUntil;

    @Column(nullable = false)
    private boolean systemAccount = false;

    // User (M) : (1) Role
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id") // Owns the foreign key (it has @JoinColumn)
    private Role role;

    // User (1) : (1) Staff
    @OneToOne(mappedBy = "user")
    private Staff staff;

    // User (1) : (1) Customer
    @OneToOne(mappedBy = "user")
    private Customer customer;

    // Helper Methods
    public void increaseFailedLoginAttempts() {
        this.failedLoginAttempts++;
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }

    public boolean isAccountLocked() {
        return accountLockedUntil != null
                && accountLockedUntil.isAfter(LocalDateTime.now());
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.passwordChangedAt = LocalDateTime.now();
        this.mustChangePassword = false;
    }
}
