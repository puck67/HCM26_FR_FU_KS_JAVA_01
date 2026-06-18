package fa.training.jsfw_s_a102.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentMaterial {

    private Long id;
    private String title;
    private String description;
    private String fileName;
}
