package com.fresheracademy.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer frequency = 0;
}
