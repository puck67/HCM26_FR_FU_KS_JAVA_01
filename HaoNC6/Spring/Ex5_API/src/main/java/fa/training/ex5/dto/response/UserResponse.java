package fa.training.ex5.dto.response;

import fa.training.ex5.enums.UserStatus;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private UserStatus status;
    private Set<String> roles;
}
