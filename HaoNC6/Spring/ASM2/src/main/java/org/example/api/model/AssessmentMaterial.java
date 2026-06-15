package org.example.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class holding metadata about an uploaded assessment file.
 * Data is stored in-memory (no database required).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentMaterial {

    private long id;

    private String title;

    private String description;

    /**
     * The stored filename on the server (used for download links).
     */
    private String fileName;
}
