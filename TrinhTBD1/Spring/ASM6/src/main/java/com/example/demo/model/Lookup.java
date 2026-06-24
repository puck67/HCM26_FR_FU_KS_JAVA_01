package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Lookup type is required")
    private String type;

    @Column(nullable = false, length = 10)
    @NotBlank(message = "Lookup code is required")
    private String code;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Lookup name is required")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Position is required")
    private Integer position;
}
