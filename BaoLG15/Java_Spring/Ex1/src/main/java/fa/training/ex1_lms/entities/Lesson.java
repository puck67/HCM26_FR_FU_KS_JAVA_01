package fa.training.ex1_lms.entities;

import fa.training.ex1_lms.enums.ContentType;
import fa.training.ex1_lms.enums.LessonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column
    private ContentType content_type;

    @Enumerated(EnumType.STRING)
    @Column
    private LessonStatus status;
}
