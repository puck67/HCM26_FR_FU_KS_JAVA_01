package com.fpt.lms.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class CourseJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public List<Map<String, Object>> findRecentCoursesRaw(int limit) {
        String sql = "SELECT id, title, status, created_at FROM tbl_course ORDER BY created_at DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, limit);
    }
}
