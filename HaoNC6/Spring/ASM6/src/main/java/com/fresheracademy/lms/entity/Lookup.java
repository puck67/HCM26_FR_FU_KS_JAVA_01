package com.fresheracademy.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_lookup")
@Data
public class Lookup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // e.g., 'CourseStatus', 'ReviewStatus'

    @Column(nullable = false)
    private String name; // e.g., 'Draft', 'Published'

    @Column(nullable = false)
    private Integer code; // e.g., 1, 2
}
