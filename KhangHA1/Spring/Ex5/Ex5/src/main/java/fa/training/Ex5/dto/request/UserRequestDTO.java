package fa.training.TrainingMaterialManagementSystem.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String fullName;

    @Size(min = 6)
    private String password;

    private List<String> roles;
}