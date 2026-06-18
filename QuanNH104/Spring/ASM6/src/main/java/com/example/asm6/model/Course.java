package com.example.asm6.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    private String content; // Markdown format

    @Column(nullable = false)
    private Integer status; // 1 = DRAFT, 2 = PUBLISHED, 3 = ARCHIVED

    @Column(name = "category")
    private String category; // e.g. "HTML, CSS, Spring"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
