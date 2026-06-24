package com.example.ex2.validation;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

public class Validators {

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỶỷỸỹỹ\\s]+$"
    );

    public static void validateStudent(Student student, Errors errors) {
        // Validate Name
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            errors.rejectValue("name", "name.required", "Tên học viên không được để trống");
        } else {
            String name = student.getName().trim();
            if (!NAME_PATTERN.matcher(name).matches()) {
                errors.rejectValue("name", "name.invalid", "Tên học viên không được chứa số hay ký tự đặc biệt");
            } else if (name.length() > 100) {
                errors.rejectValue("name", "name.size", "Tên học viên không được vượt quá 100 ký tự");
            }
        }

        // Validate Age
        if (student.getAge() <= 0 || student.getAge() >= 150) {
            errors.rejectValue("age", "age.invalid", "Tuổi học viên phải nằm trong khoảng từ 1 đến 149");
        }
    }

    public static void validateCourse(Course course, Errors errors) {
        // Validate Title
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            errors.rejectValue("title", "title.required", "Tiêu đề khóa học không được để trống");
        } else {
            String title = course.getTitle().trim();
            if (title.length() > 100) {
                errors.rejectValue("title", "title.size", "Tiêu đề khóa học không được vượt quá 100 ký tự");
            }
        }

        // Validate Credit
        if (course.getCredit() <= 0) {
            errors.rejectValue("credit", "credit.invalid", "Số tín chỉ của khóa học phải lớn hơn 0");
        }
    }
}
