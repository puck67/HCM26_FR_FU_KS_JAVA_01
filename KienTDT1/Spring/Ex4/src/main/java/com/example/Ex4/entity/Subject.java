package com.example.Ex4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String subjectCode;

    private String subjectName;
    private Integer duration;

    @OneToMany(
            mappedBy = "subject",
            cascade = CascadeType.ALL
    )
    private List<Material> materials;
}