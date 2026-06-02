# Hệ thống quản lý thông tin phương tiện giao thông (Vehicle CRUD)

Dự án này là hệ thống console app Java quản lý thông tin phương tiện giao thông (**Vehicle**) cùng với hai đối tượng bổ trợ (**Manufacturer** và **Category**), sử dụng Java 21+, kết nối JDBC đến cơ sở dữ liệu **PostgreSQL** và thực thi toàn bộ các thao tác CRUD qua **Stored Procedures/Functions**. Dự án cũng đi kèm bộ kiểm thử tự động toàn diện bằng **JUnit 5**.

## Các công nghệ sử dụng
* Java SE 21
* JDBC (PostgreSQL Driver)
* PostgreSQL Database
* JUnit 5 (JUnit Jupiter)
* Maven

## Cấu trúc dự án
```
Java_Review/
├── pom.xml
├── README.md
├── sql/
│   └── vehicle_db.sql
└── src/
    ├── main/
    │   ├── java/fa/training/
    │   │   ├── Main.java
    │   │   ├── database/
    │   │   │   └── DBConnection.java
    │   │   ├── dao/
    │   │   │   ├── CategoryDAO.java
    │   │   │   ├── ManufacturerDAO.java
    │   │   │   ├── VehicleDAO.java
    │   │   │   └── impl/
    │   │   │       ├── CategoryDAOImpl.java
    │   │   │       ├── ManufacturerDAOImpl.java
    │   │   │       └── VehicleDAOImpl.java
    │   │   ├── entities/
    │   │   │   ├── Category.java
    │   │   │   ├── Manufacturer.java
    │   │   │   └── Vehicle.java
    │   │   └── util/
    │   │       ├── ConsoleUtil.java
    │   │       └── Validator.java
    │   └── resources/
    │       └── db.properties
    └── test/java/fa/training/
        ├── dao/
        │   └── VehicleDAOImplTest.java
        └── util/
            └── ValidatorTest.java
```

---

## Hướng dẫn cài đặt và thiết lập

### 1. Chuẩn bị cơ sở dữ liệu (PostgreSQL)
1. Đảm bảo rằng PostgreSQL đang chạy trên máy của bạn (mặc định tại cổng `5432`).
2. Khởi tạo một cơ sở dữ liệu mới tên là `vehicle_db`:
   ```sql
   CREATE DATABASE vehicle_db;
   ```
3. Chạy script SQL nằm tại `sql/vehicle_db.sql` vào database `vehicle_db` để khởi tạo các bảng, dữ liệu mẫu, và 5 stored procedures/functions.
   Bạn có thể dùng công cụ GUI (như pgAdmin, DBeaver) hoặc dùng command-line `psql`:
   ```bash
   psql -U postgres -d vehicle_db -f sql/vehicle_db.sql
   ```

### 2. Cấu hình kết nối cơ sở dữ liệu
Cập nhật thông tin kết nối cơ sở dữ liệu PostgreSQL của bạn tại file:
`src/main/resources/db.properties`

```properties
db.url=jdbc:postgresql://localhost:5432/vehicle_db
db.username=postgres
db.password=123456
db.driver=org.postgresql.Driver
```

---

## Hướng dẫn chạy chương trình và kiểm thử

### 1. Chạy Unit & Integration Test (JUnit 5)
Sử dụng Maven để chạy bộ kiểm thử tự động kiểm tra logic validation cùng các stored procedures:
```bash
mvn clean test
```
*Lưu ý: Tích hợp kiểm thử `VehicleDAOImplTest` yêu cầu database PostgreSQL đang hoạt động bình thường theo các cấu hình trong file `db.properties`.*

### 2. Chạy ứng dụng console (Main)
Để biên dịch và khởi động menu ứng dụng console tương tác trực tiếp:
```bash
mvn compile exec:java -Dexec.mainClass="fa.training.Main"
```

Ứng dụng cung cấp các tùy chọn menu sau:
1. **Add new record**: Nhập ID xe, Model, Price, Owner Email, Owner Phone và lựa chọn Manufacturer/Category bằng ID được liệt kê. Dữ liệu đầu vào sẽ được tự động validate toàn diện trước khi chèn vào database thông qua Stored Procedure.
2. **Display all records**: Hiển thị danh sách toàn bộ xe dưới dạng bảng được định dạng đẹp mắt trên Console.
3. **Update a record**: Cho phép cập nhật thông tin của xe dựa theo ID. Người dùng có thể nhấn Enter để giữ nguyên giá trị cũ của từng trường hoặc nhập giá trị mới để cập nhật thông qua Stored Procedure.
4. **Delete a record**: Xóa bản ghi xe theo ID sau khi xác nhận lại.
5. **Search record by ID**: Tìm kiếm và hiển thị thông tin chi tiết của xe theo ID.
6. **Exit**: Thoát khỏi ứng dụng.

---

## Mô tả Stored Procedures & Functions trong Database
1. **`insert_vehicle`** (PROCEDURE): Thêm xe mới vào bảng `vehicles`.
2. **`update_vehicle`** (PROCEDURE): Cập nhật thông tin của xe.
3. **`delete_vehicle`** (PROCEDURE): Xóa xe khỏi bảng `vehicles` dựa vào ID.
4. **`get_all_vehicles`** (FUNCTION returning TABLE): Lấy danh sách tất cả các xe đang quản lý.
5. **`find_vehicle_by_id`** (FUNCTION returning TABLE): Tìm kiếm xe tương ứng với ID cho trước.
