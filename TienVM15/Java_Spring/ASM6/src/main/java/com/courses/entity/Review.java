package com.courses.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "course")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int rating; // 1 to 5

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private int status; // 1=PENDING, 2=APPROVED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
