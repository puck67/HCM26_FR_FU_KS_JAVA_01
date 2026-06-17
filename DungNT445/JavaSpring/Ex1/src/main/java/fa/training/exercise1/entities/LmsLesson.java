package fa.training.exercise1.entities;

import fa.training.exercise1.enums.ContentType;
import fa.training.exercise1.enums.LmsLessonStatus;
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
public class LmsLesson {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "course_code", referencedColumnName = "course_code", nullable = false),
            @JoinColumn(name = "start_date", referencedColumnName = "start_date", nullable = false)
    })
    private LmsCourse lmsCourse;

    @Column(nullable = false)
    private String lesson_name;

    @Column(nullable = false)
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column
    private ContentType content_type;

    @Enumerated(EnumType.STRING)
    @Column
    private LmsLessonStatus status;
}
