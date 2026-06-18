package fa.training.assignment3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "instructor_full_name", length = 80)
    private String instructorFullName;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "total_hours")
    private int totalHours;

    /**
     * Constructor không có ID, phục vụ tạo mới.
     */
    public TrainingCourse(String courseName, String instructorFullName, String summary, int totalHours) {
        this.courseName = courseName;
        this.instructorFullName = instructorFullName;
        this.summary = summary;
        this.totalHours = totalHours;
    }

    @Override
    public String toString() {
        return String.format("TrainingCourse[id=%d, name='%s', instructor='%s', hours=%d]",
                id, courseName, instructorFullName, totalHours);
    }
}
