package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    // Custom Quaries
    boolean existsByRoleId(Long roleId);
    
    Optional<User> findByEmail(String email);

    Optional<User> findFirstBySystemAccountTrue();

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query(
        """
        SELECT DISTINCT u
        FROM User u
        JOIN FETCH u.role r
        LEFT JOIN FETCH r.rolePermissions rp
        LEFT JOIN FETCH rp.permission
        WHERE u.email = :email
        """
    )
    Optional<User> findByEmailWithRoleAndPermissions(@Param("email") String email);
}