package com.example.asm6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_lookup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lookup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lookup_type", nullable = false)
    private String type;

    @Column(name = "lookup_code", nullable = false)
    private String code;

    @Column(name = "lookup_value", nullable = false)
    private String value;
}
