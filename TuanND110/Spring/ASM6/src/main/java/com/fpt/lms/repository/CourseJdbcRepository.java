package com.fpt.lms.repository;

import com.fpt.lms.dto.CourseSummaryDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Raw JDBC repository for aggregate/stats queries that don't need JPA.
 *
 * Improvements:
 * - Returns typed {@link CourseSummaryDTO} instead of raw {@code Map<String, Object>}
 * - Uses {@link RowMapper} for type-safe result mapping
 * - Constructor injection instead of @Autowired field
 */
@Repository
public class CourseJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public CourseJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ── Count Queries ─────────────────────────────────────────────────────

    public long countPublishedCourses() {
        String sql = "SELECT COUNT(*) FROM tbl_course WHERE status = 2";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    public long countAllCourses() {
        String sql = "SELECT COUNT(*) FROM tbl_course";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    public long countPendingReviews() {
        String sql = "SELECT COUNT(*) FROM tbl_review WHERE status = 1";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    // ── Typed Query with RowMapper ────────────────────────────────────────

    /**
     * Returns recent course summaries as typed DTOs.
     *
     * <p>Uses a named {@link RowMapper} instead of raw {@code Map<String, Object>}
     * for type safety and explicit column mapping.
     *
     * @param limit number of rows to return
     * @return typed list of {@link CourseSummaryDTO}
     */
    public List<CourseSummaryDTO> findRecentCourseSummaries(int limit) {
        String sql = "SELECT id, title, status, created_at FROM tbl_course ORDER BY created_at DESC LIMIT ?";

        RowMapper<CourseSummaryDTO> rowMapper = new CourseSummaryRowMapper();
        return jdbcTemplate.query(sql, rowMapper, limit);
    }

    // ── Inner RowMapper ────────────────────────────────────────────────────

    /**
     * Type-safe RowMapper for {@link CourseSummaryDTO}.
     * Implements the Generics interface {@code RowMapper<T>} explicitly.
     */
    private static final class CourseSummaryRowMapper implements RowMapper<CourseSummaryDTO> {

        @Override
        public CourseSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CourseSummaryDTO dto = new CourseSummaryDTO();
            dto.setId(rs.getLong("id"));
            dto.setTitle(rs.getString("title"));
            dto.setStatus(rs.getInt("status"));

            java.sql.Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) {
                dto.setCreatedAt(ts.toLocalDateTime());
            }
            return dto;
        }
    }
}
