package fa.training.lms.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lessonName;
    private Integer duration;
    private String contentType;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "course_code", referencedColumnName = "courseCode"),
            @JoinColumn(name = "start_date", referencedColumnName = "startDate")
    })
    private Course course;
}
