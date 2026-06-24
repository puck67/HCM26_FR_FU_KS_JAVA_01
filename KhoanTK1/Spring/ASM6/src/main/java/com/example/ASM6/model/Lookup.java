package com.example.ASM6.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String type; // e.g. "COURSE_STATUS", "REVIEW_STATUS"

    @Column(nullable = false)
    private String code; // e.g. "1", "2"

    @Column(name = "lookup_value", nullable = false)
    private String value; // e.g. "Draft", "Published", "Archived"
}
