package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.config.enums.EventStatusEnum;
import com.example.demo.entity.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private LocalDate event_date;

    @Column(nullable = true)
    private LocalTime event_time;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "poster_image", nullable = false)
    private String posterImage; // path for posterImage

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EventStatusEnum status;
}
