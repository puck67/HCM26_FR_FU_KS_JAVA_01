package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
@IdClass(CourseId.class)
public class Course {

    @Id
    @Column(name = "course_code")
    private String course_code;

    @Id
    @Column(name = "start_date")
    private Date start_date;

    @Column(nullable = false)
    private String course_name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String instructor;
}
