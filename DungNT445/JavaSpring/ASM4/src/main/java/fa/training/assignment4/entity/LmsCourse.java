package fa.training.assignment4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lms_courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LmsCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_title", nullable = false, length = 100)
    private String courseTitle;

    @Column(name = "trainer_name", length = 50)
    private String trainerName;

    @Column(name = "course_description", length = 500)
    private String courseDescription;

    @Column(name = "duration_hours")
    private int hours;

    // Constructor without ID for convenience
    public LmsCourse(String courseTitle, String trainerName, String courseDescription, int hours) {
        this.courseTitle = courseTitle;
        this.trainerName = trainerName;
        this.courseDescription = courseDescription;
        this.hours = hours;
    }
}
