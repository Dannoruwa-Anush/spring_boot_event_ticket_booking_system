package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long>{
    // Custom Quaries
}
