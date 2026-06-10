package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code", nullable = false),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date", nullable = false)
    })
    private Course course;

    @Column(nullable = false)
    private String lesson_name;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String content_type;

    @Column(nullable = false)
    private String status;
}
