package fa.training.lms.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Course {
    @EmbeddedId
    private CourseId courseId;

    private String courseName;
    private String category;
    private String instructor;
}
