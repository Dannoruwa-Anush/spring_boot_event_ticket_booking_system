package com.example.demo.entity;

import java.util.List;

import com.example.demo.config.enums.RoleTypeEnum;
import com.example.demo.entity.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleTypeEnum name;

    // User (M) : (1) Role
    @OneToMany(mappedBy = "role") // Mirrors the relationship: used for navigation only (mappedBy = "role")
    private List<User> users;
}
