package fa.training.ex3.dto.request;

import fa.training.ex3.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotNull(message = "User ID is required")
    private Long user_id;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Full name cannot be empty")
    private String full_name;

    @NotNull(message = "Status is required")
    private UserStatus status;

    @NotEmpty(message = "At least one role must be selected")
    private Set<Long> roleIds;
}
