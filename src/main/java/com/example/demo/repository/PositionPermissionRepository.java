package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PositionPermission;

public interface PositionPermissionRepository extends JpaRepository<PositionPermission, Long>{
    // Custom Quaries
    boolean existsByPositionIdAndPermissionId(Long positionId, Long permissionId);

    boolean existsByPositionId(Long positionId);

    List<PositionPermission> findByPositionId(Long positionId);
}
