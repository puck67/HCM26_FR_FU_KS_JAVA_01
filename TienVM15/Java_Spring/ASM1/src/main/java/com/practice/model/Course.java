package com.practice.model;

import jakarta.validation.constraints.*;
import lombok.*;

// BÀI TẬP TASK 1: KHAI BÁO CLASS COURSE VỚI CÁC RÀNG BUỘC VALIDATION
// 1. Tạo các Lombok annotations: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder (nếu cần).
// 2. Khai báo các thuộc tính của lớp Course:
//    - `title` (String)
//    - `instructorName` (String)
//    - `instructorEmail` (String)
//    - `description` (String)
//    - `durationHours` (Integer)
// 3. Thêm các validation annotations (Sử dụng thư viện jakarta.validation.constraints.*):
//    - `title`: Không được để trống (Not Empty) và tối thiểu 5 ký tự (@Size(min = 5)).
//    - `instructorName`: Không được để trống (Not Empty) và tối thiểu 2 ký tự (@Size(min = 2)).
//    - `instructorEmail`: Phải đúng định dạng email (@Email) và không được để trống (Not Empty / Not Blank).
//    - `description`: Không được để trống và độ dài từ 10 đến 200 ký tự (@Size(min = 10, max = 200)).
//    - `durationHours`: Không được null (@NotNull) và giá trị tối thiểu là 1 (@Min(1)).

// VIẾT CODE CỦA BẠN DƯỚI ĐÂY:
public class Course {
}
