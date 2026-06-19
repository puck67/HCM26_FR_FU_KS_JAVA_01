package fa.training.Ex5.dto.request;

public class LoginRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}