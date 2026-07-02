package com.lms.trainingrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paged response wrapper — replaces ResponseEntity<?> wildcard.
 *
 * <p>Usage example:
 * <pre>
 *   PagedResponse&lt;CourseResponseDTO&gt; response = new PagedResponse&lt;&gt;(content, page, size, totalElements);
 * </pre>
 *
 * @param <T> The type of items in the page content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    /** Items on the current page */
    private List<T> content;

    /** Zero-based current page number */
    private int page;

    /** Number of items per page */
    private int size;

    /** Total number of items across all pages */
    private long totalElements;

    /** Total number of pages */
    private int totalPages;

    /** Whether this is the last page */
    private boolean last;

    /**
     * Convenience constructor from Spring {@link org.springframework.data.domain.Page}.
     *
     * @param springPage the Spring Data Page object
     */
    public static <T> PagedResponse<T> from(org.springframework.data.domain.Page<T> springPage) {
        return new PagedResponse<>(
            springPage.getContent(),
            springPage.getNumber(),
            springPage.getSize(),
            springPage.getTotalElements(),
            springPage.getTotalPages(),
            springPage.isLast()
        );
    }
}
