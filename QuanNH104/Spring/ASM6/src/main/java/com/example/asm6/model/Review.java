package com.example.asm6.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer rating; // 1 to 5

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Integer status; // 1 = PENDING, 2 = APPROVED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
