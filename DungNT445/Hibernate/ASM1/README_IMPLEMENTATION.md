# Kế hoạch triển khai Hibernate Assignment 1 (ASM1)

Dưới đây là phân tích chi tiết yêu cầu đề bài và các bước thực hiện để xây dựng hệ thống quản lý đào tạo sử dụng **Hibernate** và **H2 Database** (File-based), tổ chức code sạch và chia package chuẩn theo mô hình 3 lớp.

---

## 1. Phân Tích Cấu Trúc Project & Package
Project sẽ được tổ chức theo package `fa.training` nhằm tuân thủ chuẩn đặt tên package trong các bài thực hành/assignment:

*   **`fa.training.entities`**: Định nghĩa các thực thể (Entity) JPA/Hibernate.
    *   `Student`: Đại diện cho bảng `student` (id, name, age) có quan hệ Many-to-Many với `Course`.
    *   `Course`: Đại diện cho bảng `course` (id, title, credit) có quan hệ Many-to-Many với `Student`.
*   **`fa.training.dao`**: Định nghĩa các Interface truy xuất dữ liệu.
    *   `StudentDAO` & `CourseDAO`.
*   **`fa.training.dao.impl`**: Hiện thực các Interface DAO sử dụng Hibernate `SessionFactory`.
    *   `StudentDAOImpl` & `CourseDAOImpl`.
*   **`fa.training.service`**: Định nghĩa các Interface nghiệp vụ (Business logic).
    *   `StudentService` & `CourseService`.
*   **`fa.training.service.impl`**: Hiện thực các Interface Service để xử lý logic trung gian và gọi DAO.
    *   `StudentServiceImpl` & `CourseServiceImpl`.
*   **`fa.training.util`**: Các công cụ tiện ích.
    *   `HibernateUtil`: Quản lý và cung cấp `SessionFactory`.
    *   `Validator`: Kiểm tra dữ liệu đầu vào từ Console (chống lỗi nhập chữ, trống, sai range).
*   **`fa.training.main`**: Lớp chứa hàm chạy chương trình.
    *   `Main`: Quản lý hiển thị Menu với cú pháp `switch-case` lambda của Java 21.

---

## 2. Thiết Kế Cơ Sở Dữ Liệu & Quan Hệ
Chúng ta sử dụng cơ sở dữ liệu **H2 (File-based)** để dữ liệu được lưu lại sau mỗi lần tắt/mở ứng dụng, không cần cài đặt SQL Server/PostgreSQL phức tạp.

### Sơ đồ mối quan hệ (Many-to-Many):
*   **Student** có mối quan hệ `@ManyToMany` với **Course** (sở hữu bảng liên kết `student_course`).
*   **Course** có mối quan hệ `@ManyToMany(mappedBy = "courses")` với **Student**.

---

## 3. Danh Sách Các Chức Năng Menu (Sử dụng Lambda Switch Expression)

Hệ thống menu sẽ sử dụng cú pháp `switch` expression dạng lambda (`case X -> { ... }`) của Java 21 giúp code ngắn gọn, rõ ràng và không cần từ khóa `break`.

### Menu Chính (Main Menu)
1. **Quản lý sinh viên (Student Management)** -> Chuyển đến Menu Sinh viên.
2. **Quản lý môn học (Course Management)** -> Chuyển đến Menu Môn học.
3. **Quản lý đăng ký học (Enrollment Management)** -> Chuyển đến Menu Đăng ký.
4. **Truy vấn nâng cao & Báo cáo (Queries and Reports)** -> Chuyển đến Menu Truy vấn.
5. **Thoát chương trình** -> Đóng SessionFactory và dừng ứng dụng.

### Các Menu Con (Sub-Menus)
*   **Student Menu**:
    *   Thêm mới sinh viên (Có validate tên không trống, tuổi từ $10 - 100$).
    *   Cập nhật thông tin sinh viên.
    *   Xóa sinh viên (Có kiểm tra ràng buộc trước khi xóa).
    *   Xem thông tin sinh viên theo ID.
    *   Hiển thị toàn bộ danh sách sinh viên.
*   **Course Menu**:
    *   Tạo môn học mới (Validate tiêu đề không trống, số tín chỉ từ $1 - 10$).
    *   Sửa thông tin môn học.
    *   Xóa môn học.
    *   Xem môn học theo ID.
    *   Hiển thị toàn bộ môn học.
*   **Enrollment Menu**:
    *   Đăng ký môn học cho sinh viên (Liên kết sinh viên và môn học trong bảng trung gian).
    *   Hủy đăng ký môn học.
    *   Xem danh sách các môn học của một sinh viên cụ thể.
    *   Xem danh sách sinh viên tham gia một môn học cụ thể.
*   **Queries & Reports Menu** (Thực hiện các truy vấn phức tạp):
    *   Tìm sinh viên lớn hơn số tuổi yêu cầu (HQL).
    *   Tìm sinh viên theo tên (Named Query).
    *   Danh sách sinh viên kèm môn học tương ứng (HQL Join Query).
    *   Lọc môn học có tín chỉ lớn hơn mốc nhập vào (Criteria API).
    *   Thống kê số lượng sinh viên đăng ký trong từng môn (Aggregation Query).
    *   Tìm toàn bộ sinh viên đăng ký một môn học cụ thể (HQL Parameterized).

---

## 4. Quy Tắc Validate Dữ Liệu Đầu Vào
Tất cả các dữ liệu từ Console sẽ đi qua bộ lọc của lớp `Validator`:
1.  **Nhập số nguyên (`readInt`)**: Bắt lỗi `NumberFormatException` nếu người dùng nhập chữ, bắt buộc nhập lại. Kiểm tra xem số nhập vào có nằm trong khoảng `[min, max]` hay không.
2.  **Nhập chuỗi (`readString`)**: Kiểm tra không cho phép nhập chuỗi rỗng hoặc chỉ có khoảng trắng (nếu yêu cầu bắt buộc).
3.  **Tồn tại dữ liệu**: Khi thực hiện các thao tác Xóa, Sửa, Đăng ký môn học, hệ thống phải truy vấn kiểm tra thực thể có tồn tại trong database hay không trước khi thực hiện. Nếu không tồn tại, hiển thị thông báo lỗi lịch sự thay vì quăng Exception crash ứng dụng.

---

## 5. Kế Hoạch Các Bước Thực Hiện Chi Tiết (To-Do List)

- [ ] **Bước 1: Cấu hình Maven (`pom.xml`)**: Cấu hình Java 21, thêm `hibernate-core`, `h2` database, `slf4j` và `logback`.
- [ ] **Bước 2: Cấu hình Hibernate (`hibernate.cfg.xml`) & Logback (`logback.xml`)**: Cấu hình H2 file-based `./data/school_db` và đăng ký các class Entity.
- [ ] **Bước 3: Tạo các Helper Classes (`HibernateUtil`, `Validator`)**: Lấy `SessionFactory` an toàn và `Validator` kiểm soát đầu vào Console.
- [ ] **Bước 4: Định nghĩa Entities (`Student`, `Course`)**: Map Many-to-Many sử dụng JPA annotations.
- [ ] **Bước 5: Phát triển tầng DAO (`StudentDAO`, `CourseDAO`)**: Hiện thực interface & implementation với Hibernate Transaction management.
- [ ] **Bước 6: Phát triển tầng Service (`StudentService`, `CourseService`)**: Hiện thực logic điều hướng nghiệp vụ sạch.
- [ ] **Bước 7: Viết ứng dụng chính (`Main`)**: Xây dựng menu lồng nhau với Lambda switch.
