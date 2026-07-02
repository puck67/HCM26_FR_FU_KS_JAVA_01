package com.fpt.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Typed DTO for raw JDBC course summary queries.
 * Replaces untyped {@code Map<String, Object>} returned by JdbcTemplate.queryForList().
 *
 * <p>Used exclusively in {@link com.fpt.lms.repository.CourseJdbcRepository}
 * to maintain type safety without loading full JPA entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSummaryDTO {

    private Long id;
    private String title;
    private Integer status;
    private LocalDateTime createdAt;
}
