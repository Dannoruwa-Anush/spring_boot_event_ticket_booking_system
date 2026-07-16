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
@Table(name = "customers")
@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Data // getters & setters
@EqualsAndHashCode(callSuper = false)
public class Customer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String date_of_birth;

    @Column(nullable = false)
    private String phoneNo;

    // User (1) : (1) Customer
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
