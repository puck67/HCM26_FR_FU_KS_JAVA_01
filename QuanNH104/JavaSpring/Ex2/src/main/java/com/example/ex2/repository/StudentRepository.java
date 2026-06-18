package com.example.ex2.repository;

import com.example.ex2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Tìm kiếm sinh viên theo tên (không phân biệt hoa thường)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Tìm sinh viên lớn tuổi hơn một mức cụ thể
    @Query("SELECT s FROM Student s WHERE s.age > :age")
    List<Student> findStudentsOlderThan(@Param("age") int age);
}
