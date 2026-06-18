package com.lms.securitymanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserForm {

    private Long userId;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    // Password is only checked manually in the controller if creating a new user
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private Boolean status = true;

    @NotEmpty(message = "Please select at least one role")
    private List<Long> roleIds;
}
