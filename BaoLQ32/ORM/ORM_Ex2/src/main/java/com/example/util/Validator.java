package com.example.util;

import com.example.entity.Course;
import com.example.entity.Student;

// Lớp kiểm tra tính hợp lệ của dữ liệu Student và Course
public class Validator {

    // --- CÁC HÀM KIỂM TRA CHUNG ---

    // Kiểm tra null
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null.");
        }
    }

    // Kiểm tra chuỗi rỗng
    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
    }

    // Kiểm tra độ dài tối đa
    public static void requireMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(
                fieldName + " must not exceed " + maxLength + " characters. Current: " + value.length()
            );
        }
    }

    // Kiểm tra giá trị số nằm trong khoảng
    public static void requireInRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                fieldName + " must be between " + min + " and " + max + ". Got: " + value
            );
        }
    }

    // --- VALIDATE CHO STUDENT ---

    // Kiểm tra tên sinh viên (chỉ chứa chữ cái và khoảng trắng, không chứa số/kí tự đặc biệt)
    public static void validateStudentName(String name) {
        requireNonEmpty(name, "Student name");
        requireMaxLength(name, 50, "Student name");
        if (!name.matches("^[a-zA-Z\\s\\p{L}]+$")) {
            throw new IllegalArgumentException("Student name must contain only letters and spaces. Got: " + name);
        }
    }

    // Kiểm tra tuổi sinh viên (từ 18 đến 100)
    public static void validateStudentAge(int age) {
        requireInRange(age, 18, 100, "Student age");
    }

    // Kiểm tra toàn bộ đối tượng Student
    public static void validateStudent(Student student) {
        requireNonNull(student, "Student");
        validateStudentName(student.getName());
        validateStudentAge(student.getAge());
    }

    // --- VALIDATE CHO COURSE ---

    // Kiểm tra tiêu đề khóa học
    public static void validateCourseTitle(String title) {
        requireNonEmpty(title, "Course title");
        requireMaxLength(title, 100, "Course title");
    }

    // Kiểm tra số tín chỉ khóa học (từ 1 đến 10)
    public static void validateCourseCredit(int credit) {
        requireInRange(credit, 1, 10, "Course credits");
    }

    // Kiểm tra toàn bộ đối tượng Course
    public static void validateCourse(Course course) {
        requireNonNull(course, "Course");
        validateCourseTitle(course.getTitle());
        validateCourseCredit(course.getCredit());
    }
}
