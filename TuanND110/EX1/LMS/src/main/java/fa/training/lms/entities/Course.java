package fa.training.lms.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @JsonCreator
    public Course() {
    }

    @EmbeddedId
    private CourseId id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "category")
    private String category;

    @Column(name = "instructor")
    private String instructor;

    // Một khóa học có nhiều bài học
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("course")
    private List<Lesson> lessons;


}
