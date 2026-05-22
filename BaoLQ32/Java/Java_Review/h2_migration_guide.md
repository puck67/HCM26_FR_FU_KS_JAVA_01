# 📦 Hướng dẫn sử dụng H2 Database — BookManager

Dự án đã được cấu hình thành công để sử dụng **H2 Embedded Database** thay cho SQL Server.

## 🌟 Lợi ích của H2 Embedded Database
1. **Không cần cài đặt**: Không cần cài đặt SQL Server, SSMS hay Docker. Driver tự tạo file database cục bộ.
2. **Tự động tạo bảng**: Khi chạy ứng dụng, bảng `books` tự động được tạo nếu chưa tồn tại.
3. **Mọi dữ liệu được lưu cục bộ**: Dữ liệu lưu trong file `./data/bookmanager.mv.db` nằm ngay trong thư mục dự án.

---

## 🚀 Hướng dẫn biên dịch và khởi chạy

Bạn có thể chạy dự án thông qua Terminal của PowerShell hoặc CMD bằng cách sử dụng các lệnh dưới đây:

### 1. Biên dịch dự án (Compile)
Biên dịch toàn bộ source code của ứng dụng và Unit Tests:
```powershell
& "C:\Users\Boss\.jdks\openjdk-25.0.2\bin\javac.exe" -d bin -cp "lib/*" -sourcepath src src\fa\training\main\Main.java src\fa\training\test\BookServiceTest.java
```

### 2. Chạy ứng dụng Console (Run Main App)
Chạy ứng dụng với classpath trỏ tới thư viện H2:
```powershell
& "C:\Users\Boss\.jdks\openjdk-25.0.2\bin\java.exe" -cp "bin;lib/h2-2.3.232.jar" fa.training.main.Main
```

### 3. Chạy Unit Tests (JUnit 5)
Chạy toàn bộ các test cases tự động bằng JUnit 5:
```powershell
& "C:\Users\Boss\.jdks\openjdk-25.0.2\bin\java.exe" -jar lib\junit-platform-console-standalone-1.10.2.jar -cp "bin;lib/h2-2.3.232.jar" --select-class fa.training.test.BookServiceTest
```

---

## 🛠 Cách kết nối kỹ thuật (H2 Config)

### `DBConnection.java`
- **URL**: `jdbc:h2:./data/bookmanager;AUTO_SERVER=TRUE`
  - `./data/bookmanager` -> file database sẽ được tạo tại thư mục `data/` trong thư mục dự án.
  - `AUTO_SERVER=TRUE` -> Cho phép nhiều ứng dụng/tiến trình (ví dụ: Console App và Unit Test) kết nối cùng một lúc.
- **User/Password**: `sa` / *(để trống)*
- **Tự động khởi tạo**: Lần đầu ứng dụng kết nối, block `static` trong `DBConnection.java` sẽ thực thi câu lệnh SQL tạo bảng:
  ```sql
  CREATE TABLE IF NOT EXISTS books (
      id VARCHAR(10) PRIMARY KEY,
      title VARCHAR(100) NOT NULL,
      author VARCHAR(100) NOT NULL,
      email VARCHAR(100) NOT NULL,
      phone VARCHAR(20) NOT NULL,
      price DOUBLE NOT NULL,
      quantity INT NOT NULL,
      category VARCHAR(50) NOT NULL
  )
  ```

### `BookDAO.java`
- Đã được cập nhật sử dụng các câu lệnh SQL chuẩn (`PreparedStatement`) thay vì các hàm `CallableStatement` gọi stored procedures của SQL Server cũ.
