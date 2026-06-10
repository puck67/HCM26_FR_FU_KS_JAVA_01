package fa.training.ex1_lms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseId implements Serializable {
    private String course_code;
    private Date start_date;
}
