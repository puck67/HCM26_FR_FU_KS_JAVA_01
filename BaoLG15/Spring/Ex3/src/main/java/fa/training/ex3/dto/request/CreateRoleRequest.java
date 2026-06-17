package fa.training.ex3.dto.request;

import fa.training.ex3.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequest {
    @NotNull(message = "Role name cannot be null")
    private RoleEnum role_name;
}
