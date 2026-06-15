package com.fresheracademy.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_instructor")
@Data
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
