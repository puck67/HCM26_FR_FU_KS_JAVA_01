package com.courses.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_lookup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int code;

    @Column(nullable = false)
    private String type; // e.g. "CourseStatus" or "ReviewStatus"

    @Column(nullable = false)
    private int position;
}
