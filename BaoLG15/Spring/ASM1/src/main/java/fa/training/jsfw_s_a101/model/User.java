package fa.training.jsfw_s_a101.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotEmpty(message = "First name must not be empty")
    @Size(min = 2, message = "First name should have at least 2 characters")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @Size(min = 2, message = "Last name should have at least 2 characters")
    private String lastName;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters long")
    private String password;

    @Min(value = 18, message = "Age must be greater than or equal to 18")
    private int age;
}
