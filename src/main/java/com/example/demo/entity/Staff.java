package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.config.enums.StaffEmploymentStatusEnum;
import com.example.demo.entity.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "staff")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Staff extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String employeeNo; // Format emp**** - *** auto assign

    @Column(nullable = false, unique = true)
    private String nic;

    @Column(nullable = false)
    private String phoneNo;

    @Column(nullable = false)
    private LocalDateTime hire_date;

    @Column(nullable = true)
    private LocalDateTime termination_date;

    @Enumerated(EnumType.STRING)
    private StaffEmploymentStatusEnum employmentStatus;

    // User (1) : (1) Staff
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Position (M) : (1) Staff
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;
}
