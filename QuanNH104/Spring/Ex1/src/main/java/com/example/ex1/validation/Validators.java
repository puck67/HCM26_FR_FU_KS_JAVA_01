package com.example.ex1.validation;

import com.example.ex1.entity.Course;
import com.example.ex1.entity.CourseId;
import com.example.ex1.entity.Lesson;
import org.springframework.validation.Errors;

public class Validators {

    public static void validateCourse(Course course, Errors errors) {
        // Validate CourseId
        CourseId id = course.getId();
        if (id == null) {
            errors.rejectValue("id", "id.required", "Thông tin khóa chính không được để trống");
        } else {
            if (id.getCourseCode() == null || id.getCourseCode().trim().isEmpty()) {
                errors.rejectValue("id.courseCode", "courseCode.required", "Mã khóa học không được để trống");
            } else if (id.getCourseCode().length() < 2 || id.getCourseCode().length() > 30) {
                errors.rejectValue("id.courseCode", "courseCode.size", "Mã khóa học phải từ 2 đến 30 ký tự");
            }

            if (id.getStartDate() == null) {
                errors.rejectValue("id.startDate", "startDate.required", "Ngày khai giảng không được để trống");
            }
        }

        // Validate courseName
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            errors.rejectValue("courseName", "courseName.required", "Tên khóa học không được để trống");
        } else if (course.getCourseName().length() > 200) {
            errors.rejectValue("courseName", "courseName.size", "Tên khóa học không được vượt quá 200 ký tự");
        }

        // Validate category
        if (course.getCategory() == null || course.getCategory().trim().isEmpty()) {
            errors.rejectValue("category", "category.required", "Danh mục không được để trống");
        }

        // Validate instructor
        if (course.getInstructor() == null || course.getInstructor().trim().isEmpty()) {
            errors.rejectValue("instructor", "instructor.required", "Tên giảng viên không được để trống");
        }
    }

    public static void validateLesson(Lesson lesson, Errors errors) {
        // Validate lessonName
        if (lesson.getLessonName() == null || lesson.getLessonName().trim().isEmpty()) {
            errors.rejectValue("lessonName", "lessonName.required", "Tên bài học không được để trống");
        }

        // Validate duration
        if (lesson.getDuration() == null) {
            errors.rejectValue("duration", "duration.required", "Thời lượng không được để trống");
        } else {
            if (lesson.getDuration() < 1) {
                errors.rejectValue("duration", "duration.min", "Thời lượng phải tối thiểu là 1 phút");
            } else if (lesson.getDuration() > 1000) {
                errors.rejectValue("duration", "duration.max", "Thời lượng tối đa là 1000 phút");
            }
        }

        // Validate contentType
        if (lesson.getContentType() == null || lesson.getContentType().trim().isEmpty()) {
            errors.rejectValue("contentType", "contentType.required", "Loại nội dung không được để trống");
        }

        // Validate status
        if (lesson.getStatus() == null || lesson.getStatus().trim().isEmpty()) {
            errors.rejectValue("status", "status.required", "Trạng thái không được để trống");
        }
    }
}
