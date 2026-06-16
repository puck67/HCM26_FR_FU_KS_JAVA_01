package com.example.ex2.repository;

import com.example.ex2.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    // Tìm kiếm khóa học có số tín chỉ lớn hơn hoặc bằng mức cụ thể
    List<Course> findByCreditGreaterThanEqual(int credit);

    // Truy vấn tổng số học sinh của mỗi khóa học (HQL Aggregation)
    @Query("SELECT c, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title, c.credit")
    List<Object[]> findCourseStudentCount();
}
