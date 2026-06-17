package fa.training.jsfw_s_a101.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title should have at least 5 characters")
    private String title;

    @NotEmpty(message = "Instructor name must not be empty")
    @Size(min = 2, message = "Instructor name should have at least 2 characters")
    private String instructorName;

    @NotEmpty(message = "Instructor email must not be empty")
    @Email(message = "Instructor email must be a valid email address")
    private String instructorEmail;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters long")
    private String description;

    @NotNull(message = "Duration hours must not be null")
    @Min(value = 1, message = "Duration hours must be at least 1")
    private Integer durationHours;
}
