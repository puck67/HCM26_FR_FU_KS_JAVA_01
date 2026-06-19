package com.example.Ex4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String storedFileName;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadDate;
    private String description;

    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
}
