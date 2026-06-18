package fa.training.exercise1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor

public class LmsCourseId implements Serializable {
    public LmsCourseId(String course_code, Date start_date) { this.course_code = course_code; this.start_date = start_date; }

    private String course_code;
    private Date start_date;
}
