package fa.training.exercise1.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "courses")
@IdClass(LmsCourseId.class)
@NoArgsConstructor
@AllArgsConstructor
public class LmsCourse {
    public Date getStart_date() { return start_date; }
    public String getCourse_code() { return course_code; }


    @Id
    @NotEmpty(message = "Mã khóa học không được để trống")
    @Column(name = "course_code", nullable = false)
    private String course_code;

    @Id
    @NotNull(message = "Ngày khai giảng không được để trống")
    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start_date;

    @NotEmpty(message = "Tên khóa học không được để trống")
    @Column(name = "course_name", nullable = false)
    private String course_name;

    @NotEmpty(message = "Chuyên ngành không được để trống")
    @Column(name = "category", nullable = false)
    private String category;

    @NotEmpty(message = "Giảng viên không được để trống")
    @Column(name = "instructor", nullable = false)
    private String instructor;
}
