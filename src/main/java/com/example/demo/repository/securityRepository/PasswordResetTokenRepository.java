package com.example.demo.repository.securityRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;
import com.example.demo.entity.securityEntity.PasswordResetToken;

public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Long> {

    //Custom Quaries
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    Optional<PasswordResetToken> findTopByUserOrderByIdDesc(User user);
}