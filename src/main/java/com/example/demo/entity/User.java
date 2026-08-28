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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Getter
@Setter
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
    @Column(name = "must_change_password", nullable = false)
    private boolean mustChangePassword = false;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "account_locked_until", nullable = true)
    private LocalDateTime accountLockedUntil;

    @Column(name = "system_account", nullable = false)
    private boolean systemAccount = false;

    // User (M) : Role (1) 
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id") // Owns the foreign key (it has @JoinColumn)
    private Role role;

    // User (1) : Staff (1) 
    @OneToOne(mappedBy = "user")
    private Staff staff;

    // User (1) : Customer (1) 
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

    public void unlockAccount() {
        this.accountLockedUntil = null;
        this.failedLoginAttempts = 0;
    }
}
