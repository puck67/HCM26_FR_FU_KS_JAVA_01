# LMS Spring Framework Core - Assignment 1

## Cấu trúc project

```
lms-spring/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/lms/
        │       ├── model/
        │       │   └── Course.java          ← Task 1
        │       ├── service/
        │       │   └── CourseService.java   ← Task 2
        │       └── main/
        │           └── Test.java            ← Task 3 (entry point)
        └── resources/
```

## Spring Concepts được sử dụng

| Concept | Áp dụng |
|---|---|
| IoC Container | `AnnotationConfigApplicationContext` trong `main()` |
| Component Scan | `@ComponentScan(basePackages = "com.lms")` |
| Bean | `@Service` trên `CourseService`, `@Component` trên `Test` |
| Dependency Injection | `@Autowired` inject `CourseService` vào `Test` |

## Cách build và chạy

### Yêu cầu
- JDK 1.8+
- Maven 3+

### Build
```bash
cd lms-spring
mvn clean package
```

### Chạy
```bash
java -jar target/lms-spring-1.0-SNAPSHOT.jar
```

Hoặc chạy trực tiếp qua Maven:
```bash
mvn exec:java -Dexec.mainClass="com.lms.main.Test"
```

## Expected Output

```
========================================
   LMS - Learning Management System
========================================

--- Saving courses to file ---
>>> Đã lưu 3 course(s) vào file: courses.dat

--- Loading courses from file ---
>>> Đã đọc 3 course(s) từ file: courses.dat

--- Course List ---
[Course: Spring Framework] by John Doe (40 hours): Learn Spring Core, MVC, and Boot
[Course: Java OOP] by Jane Smith (30 hours): Master Object-Oriented Programming in Java
[Course: Microservices with Spring Boot] by Bob Johnson (50 hours): Build scalable microservices using Spring Boot and Docker

========================================
   Done!
========================================
```

## Giải thích flow

1. `main()` → Khởi tạo Spring IoC container với `AppConfig` (`@ComponentScan`)
2. Spring scan package `com.lms` → tìm thấy `CourseService` (`@Service`) và `Test` (`@Component`)
3. Spring tạo bean `CourseService` → inject vào field `courseService` của `Test` (qua `@Autowired`)
4. `test.run()` → tạo 3 Course → gọi `courseService.saveCourses()` → ghi vào `courses.dat`
5. Gọi `courseService.getCourses()` → đọc từ `courses.dat` → in ra bằng `displayInfo()`
