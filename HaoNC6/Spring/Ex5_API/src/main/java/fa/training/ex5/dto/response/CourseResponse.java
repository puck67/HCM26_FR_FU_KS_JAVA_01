package fa.training.ex5.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long courseId;
    private String courseName;
    private Integer duration;
    private String description;
}
