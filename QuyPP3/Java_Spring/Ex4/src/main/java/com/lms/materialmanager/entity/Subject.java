package com.lms.materialmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @NotBlank(message = "Subject code is required")
    @Column(name = "subject_code", unique = true, nullable = false)
    private String subjectCode;

    @NotBlank(message = "Subject name is required")
    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @NotNull(message = "Duration is required")
    @Column(nullable = false)
    private Integer duration;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Material> materials = new ArrayList<>();
}
