package com.lms.trainingrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for simple message responses.
 * Replaces the anti-pattern of referencing UserController.MapResponse from other controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessageResponse {
    private String message;
}
