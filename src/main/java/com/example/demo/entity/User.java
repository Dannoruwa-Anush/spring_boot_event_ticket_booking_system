package com.example.demo.entity;

import com.example.demo.entity.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
 public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // User (1) : (1) Role
    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id") // Owns the foreign key (it has @JoinColumn)
    private Role role;
}
