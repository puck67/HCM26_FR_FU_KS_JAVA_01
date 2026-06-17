package fa.training.ex4.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubjectRequest {
    @NotBlank(message = "Subject code is required")
    private String subject_code;

    @NotBlank(message = "Subject name is required")
    private String subject_name;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than or equal to 1")
    private Integer duration;
}
