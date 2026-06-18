package fa.training.ex3.dto.request;

import fa.training.ex3.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {
    @NotNull(message = "Role ID is required")
    private Long role_id;

    @NotNull(message = "Role name cannot be null")
    private RoleEnum role_name;
}
