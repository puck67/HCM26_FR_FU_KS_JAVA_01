# Ex 2 - Dynamic Menu Management System

Hệ thống quản lý menu động cho ứng dụng đào tạo (Spring Boot, Spring Data JPA, Thymeleaf, H2 Database).

## 🌟 Tính năng chính
1. **Menu Động Từ Database**: Sidebar hoàn toàn tự động render từ database thông qua Thymeleaf `th:each`, không hard-code HTML.
2. **Hỗ trợ Menu 3 cấp (Bonus)**: Renders nested menu đa cấp (Root -> Sub-menu -> Sub-sub-menu) bằng cách sử dụng các thẻ collapse lồng nhau.
3. **Phân quyền Menu theo Role (Bonus)**:
   - **Admin**: Dashboard, User Management, System Management.
   - **Teacher**: Dashboard, Training Management, Exam Management.
   - **Student**: Dashboard, Result, My Courses.
   - Bộ chuyển đổi Role (Role Switcher) trực quan ở góc phải thanh topnav giúp chuyển đổi nhanh chóng và trải nghiệm sidebar thay đổi tức thì.
4. **Quản lý CRUD Menu**:
   - Thêm mới/Chỉnh sửa Menu (Tên, URL, Icon, Menu cha, Thứ tự hiển thị, Trạng thái hoạt động, Quyền truy cập).
   - Trình chọn Icon (Icon Picker) trực quan với Bootstrap Icons.
5. **Xử lý xóa an toàn (Conditional/Cascade Delete)**:
   - Nếu menu có menu con: Hệ thống sẽ ngăn chặn xóa và đưa ra cảnh báo.
   - Admin có quyền lựa chọn: Hủy thao tác xóa hoặc **Cascade Delete** (Xóa sạch menu cha và toàn bộ menu con của nó).
6. **Bảo vệ Vòng lặp Menu (Anti-circular Parent)**:
   - Khi tạo/sửa Menu, danh sách Menu Cha trong Dropdown sẽ tự động loại bỏ menu hiện tại và toàn bộ các menu con của nó để tránh lỗi vòng lặp cha-con (Circular Hierarchy).

---

## 📂 Danh sách các File được tạo dựng

### 1. Model & Repository
- **[`Menu.java`](src/main/java/com/menu/model/Menu.java)**:
  - Entity chứa self-reference `@ManyToOne` (parent) và `@OneToMany` (children).
  - Tích hợp hàm `getDepth()` để tính độ sâu của nút trong cây (0: Root, 1: Sub, 2: Sub-sub).
  - Tích hợp hàm `hasRole(roleName)` để kiểm tra quyền truy cập.
- **[`MenuRepository.java`](src/main/java/com/menu/repository/MenuRepository.java)**:
  - Interface JPA xử lý truy vấn Menu.

### 2. Service
- **[`MenuService.java`](src/main/java/com/menu/service/MenuService.java)**:
  - `@PostConstruct seedInitialData()`: Tự động khởi tạo dữ liệu mẫu giống yêu cầu đề bài và bổ sung thêm menu cấp 3.
  - `getHierarchicalMenus()`: Chuyển đổi cây menu thành dạng phẳng (Preorder Traversal) để hiển thị trên bảng quản lý đúng theo thứ tự phân cấp.
  - `deleteMenu(id, cascade)`: Xử lý logic xóa (chặn xóa nếu có menu con hoặc xóa cascade).

### 3. Controllers
- **[`GlobalControllerAdvice.java`](src/main/java/com/menu/controller/GlobalControllerAdvice.java)**:
  - `@ControllerAdvice` tự động cung cấp danh sách `sidebarMenus` và `currentRole` cho tất cả các trang, giúp Sidebar hoạt động ở mọi URL mà không cần viết lại code trong từng Controller.
- **[`DashboardController.java`](src/main/java/com/menu/controller/DashboardController.java)**:
  - Thống kê các số liệu (Tổng số menu, menu cha, menu con).
  - Endpoint `/change-role` lưu role hiện tại vào HTTP Session.
  - Đăng ký mock routing cho các link menu để test điều hướng thành công.
- **[`MenuController.java`](src/main/java/com/menu/controller/MenuController.java)**:
  - Xử lý các endpoint danh sách, thêm, sửa, lưu và xóa menu.

### 4. UI Templates & Static Resources
- **[`style.css`](src/main/resources/static/css/style.css)**: Định nghĩa giao diện premium, hiệu ứng hover mượt mà, màu sắc hiện đại và ký hiệu phân cấp cây `├─` & `└─`.
- **[`layout.html`](src/main/resources/templates/layout.html)**: Layout tổng thể chứa Sidebar động 3 cấp và thanh topnav chứa Role Switcher.
- **[`dashboard.html`](src/main/resources/templates/dashboard.html)**: Trang chính hiển thị số liệu thống kê.
- **[`mock.html`](src/main/resources/templates/mock.html)**: Trang đích hiển thị kết quả điều hướng thành công từ các menu link.
- **[`list.html`](src/main/resources/templates/menu/list.html)**: Trang quản lý danh sách menu.
- **[`form.html`](src/main/resources/templates/menu/form.html)**: Form thêm/sửa menu với trình chọn icon trực quan.

---

## 🛠️ Hướng dẫn Khởi chạy trong IntelliJ IDEA
1. Mở thư mục `Ex2` trong **IntelliJ IDEA** (hoặc IDE Java khác).
2. IDE sẽ tự động nhận diện dự án Maven và tải các dependencies khai báo trong `pom.xml`.
3. Tìm file **`MenuApplication.java`** tại thư mục `src/main/java/com/menu/MenuApplication.java`.
4. Click chuột phải chọn **Run 'MenuApplication.main()'**.
5. Mở trình duyệt và truy cập: [**`http://localhost:8081/`**](http://localhost:8081/).

---

## 🛢️ Hướng dẫn Truy cập Database Console (H2)
- URL H2 Console: [`http://localhost:8081/h2-console`](http://localhost:8081/h2-console)
- JDBC URL: `jdbc:h2:mem:menudb`
- Username: `sa`
- Password: (để trống)
- Nhấp **Connect** để truy cập bảng `MENUS` và chạy các câu lệnh SQL truy vấn trực tiếp.
