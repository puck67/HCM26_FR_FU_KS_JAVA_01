package fa.training.ex4.dto.response;

import fa.training.ex4.entities.Material;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private Long subject_id;
    private String subject_code;
    private String subject_name;
    private Integer duration;
    private List<Material> materials;
}
