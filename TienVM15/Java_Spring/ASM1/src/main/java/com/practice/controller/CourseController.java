package com.practice.controller;

import com.practice.model.Course;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// BÀI TẬP TASK 3: TẠO CONTROLLER XỬ LÝ FORM ĐĂNG KÝ KHÓA HỌC
// 1. Đánh dấu class này là Controller của Spring MVC.
// 2. Viết API GET "/courses/new":
//    - Trả về trang giao diện "create_course" (Thymeleaf template).
//    - Đừng quên add một đối tượng `new Course()` mới vào Model với key là "course" để liên kết với form.
// 3. Viết API POST "/courses/create":
//    - Nhận dữ liệu gửi lên và kích hoạt validate bằng `@Valid @ModelAttribute("course") Course course`.
//    - Sử dụng `BindingResult` để hứng kết quả kiểm tra lỗi.
//    - Nếu `bindingResult.hasErrors()` là true -> Trả về giao diện "create_course" (lúc này các error message sẽ tự động hiển thị).
//    - Nếu không có lỗi -> Thực hiện chuyển hướng (redirect) tới trang thông báo thành công: "redirect:/courses/success".
// 4. Viết API GET "/courses/success":
//    - Trả về giao diện trang thành công: "create_success" (Thymeleaf template).

// VIẾT CODE CỦA BẠN DƯỚI ĐÂY:
public class CourseController {
}
