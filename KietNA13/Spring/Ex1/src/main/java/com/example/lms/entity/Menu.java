package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    private String icon;

    @Column(nullable = false)
    private String role; // e.g., ADMIN, TEACHER, STUDENT

    @Column(nullable = false)
    private Integer orderIndex;
}
