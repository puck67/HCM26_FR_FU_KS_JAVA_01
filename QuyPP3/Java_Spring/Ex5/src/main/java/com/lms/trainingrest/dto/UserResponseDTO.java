package com.lms.trainingrest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String status;
    private Set<String> roles;
}
