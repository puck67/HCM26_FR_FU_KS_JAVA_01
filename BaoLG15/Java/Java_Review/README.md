# Chương trình quản lý phim bằng Console

Đây là một chương trình quản lý phim nhỏ gọn chạy trên màn hình đen Console. Chương trình này giúp bạn có thể thêm, sửa, xóa, và tìm kiếm các thông tin về phim, đạo diễn cũng như thể loại phim.

Dưới đây là hướng dẫn chi tiết, từng bước một để bạn có thể dễ dàng cài đặt và chạy thử chương trình này ngay trên máy tính của mình nhé.

## 🛠 Hướng dẫn cài đặt và chạy chương trình

### Bước 1: Chuẩn bị cơ sở dữ liệu (Database)
Vì chương trình này lưu dữ liệu trong PostgreSQL nên bạn cần làm theo hai bước nhỏ sau đây:

1. **Tạo một database trống:** Bạn mở công cụ quản lý PostgreSQL của bạn lên, ví dụ như pgAdmin hoặc DBeaver, hoặc cũng có thể dùng Terminal, Command Prompt rồi gõ lệnh:
   ```sql
   CREATE DATABASE movie_db;
   ```
2. **Nạp dữ liệu mẫu:** Trong thư mục gốc của project, bạn sẽ thấy một thư mục tên là `sql` có chứa file `movie_db.sql`. Bạn hãy mở file này ra, copy toàn bộ nội dung bên trong rồi chạy vào database `movie_db` vừa tạo ở trên.
   Hoặc nếu bạn thích dùng dòng lệnh thì có thể gõ nhanh:
   ```bash
   psql -U postgres -d movie_db -f sql/movie_db.sql
   ```
   *Bước này sẽ tự động tạo các bảng cần thiết và nạp sẵn cho bạn một vài đạo diễn, thể loại cùng vài bộ phim mẫu để bạn vọc vạch luôn cho tiện.*

### Bước 2: Cấu hình kết nối
Giờ bạn cần chỉ đường cho chương trình biết database của bạn đang nằm ở đâu và mật khẩu truy cập là gì. Bạn mở file `db.properties` nằm ở đường dẫn: `src/main/resources/db.properties`. Sau đó, bạn sửa lại dòng `db.password` thành đúng mật khẩu PostgreSQL trên máy của bạn. 

Ví dụ cụ thể như sau:
```properties
db.url=jdbc:postgresql://localhost:5432/movie_db
db.username=postgres
db.password=123456 # (bạn nhớ thay mật khẩu của bạn vào chỗ này nhé)
db.driver=org.postgresql.Driver
```

### Bước 3: Chạy chương trình
Bạn có thể chọn một trong hai cách phổ biến nhất sau đây:

**Cách 1: Chạy bằng các phần mềm IDE** (IntelliJ IDEA, Eclipse hoặc VSCode)
Bạn chỉ cần mở project lên, tìm đến file `Main.java` theo đường dẫn `src/main/java/fa/training/Main.java`, rồi bấm vào nút hình tam giác màu xanh **Run** là xong.

**Cách 2: Chạy bằng cửa sổ dòng lệnh (Terminal / CMD)**
Bạn mở Terminal ngay tại thư mục của project và gõ lệnh để biên dịch code trước:
```bash
mvn clean compile
```
Sau đó, bạn gõ tiếp lệnh để chạy chương trình:
```bash
mvn exec:java -Dexec.mainClass="fa.training.Main"
```

## 🧪 (Tùy chọn) Chạy Test kiểm tra lỗi
Nếu bạn muốn xem chương trình có đang hoạt động tốt và ổn định hay không, bạn có thể chạy bộ kiểm thử tự động Unit Test bằng lệnh:
```bash
mvn test
```

## ✨ Điểm nổi bật của chương trình
- **Tự động tạo ID:** Khi bạn thêm một bộ phim mới, chương trình sẽ tự động dò xem trong database có phim nào mang mã số cao nhất rồi tự động đánh số tiếp theo cho bạn (ví dụ: từ `MOV001` lên `MOV002`), rất tiện lợi.
- **Xử lý lỗi mượt mà:** Nếu bạn lỡ tay gõ chữ vào những chỗ bắt buộc phải điền số, chương trình sẽ lịch sự nhắc bạn nhập lại đàng hoàng chứ không bị sập hay văng lỗi.
- **Sử dụng 100% Stored Procedures:** Ở dưới phần lõi, tất cả việc thêm/sửa/xóa đều dùng các hàm SQL với tổng cộng 19 procedures và functions, giúp đảm bảo tính an toàn dữ liệu rất cao.

---