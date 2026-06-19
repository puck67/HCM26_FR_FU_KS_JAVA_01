# LMS Assessment - File Upload & Download (JSFW_S_A102)

## Cấu trúc project (Maven)

```
lms2/
├── pom.xml
└── src/main/java/com/fsoft/lms/
    ├── LmsAssessmentApplication.java
    ├── model/
    │   └── AssessmentMaterial.java
    ├── service/
    │   ├── StorageService.java
    │   ├── StorageException.java
    │   └── StorageFileNotFoundException.java
    └── controller/
        └── AssessmentController.java
└── src/main/resources/
    ├── application.properties
    └── templates/
        └── assessments.html
```

## Build bằng Maven

```bash
mvn clean package
```

Tệp jar sẽ được tạo tại `target/lms-assessment-0.0.1-SNAPSHOT.jar`.

## Chạy ứng dụng

```bash
mvn spring-boot:run
```

hoặc

```bash
java -jar target/lms-assessment-0.0.1-SNAPSHOT.jar
```

Ứng dụng chạy tại: `http://localhost:8080/assessments`

## Chức năng

- **GET /assessments**: Hiển thị form upload và danh sách tài liệu đã tải lên.
- **POST /assessments/upload**: Nhận `title`, `description`, `file` (multipart), lưu file vào thư mục `./uploads`, lưu metadata vào danh sách in-memory, redirect về `/assessments`.
- **GET /assessments/download/{filename}**: Tải file đã upload theo tên file lưu trên server.

## Lưu ý

- File được lưu với tên ngẫu nhiên (UUID) kèm phần mở rộng gốc để tránh trùng tên/đè file.
- Thư mục lưu file (`./uploads`) được khởi tạo tự động khi ứng dụng khởi động (`@PostConstruct`).
- Nếu upload thất bại (file rỗng, lỗi I/O...), thông báo lỗi sẽ được hiển thị qua `RedirectAttributes` (flash message) trên trang `/assessments`.
- Cấu hình kích thước file tối đa trong `application.properties`:
  - `spring.servlet.multipart.max-file-size=10MB`
  - `spring.servlet.multipart.max-request-size=10MB`
