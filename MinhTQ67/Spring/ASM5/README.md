# LMS Spring Framework - User Management (Assignment 5)

## Cấu trúc project

```
lms-spring-asm5/
├── pom.xml
└── src/main/java/com/lms/
    ├── model/
    │   ├── LmsUser.java       ← Task 1 (base class, Serializable)
    │   ├── Instructor.java    ← Task 2 (extends LmsUser, override printInfo)
    │   └── Student.java       ← Task 3 (độc lập, Serializable)
    ├── service/
    │   └── LmsDataService.java ← Task 4 (@Service bean, IO file)
    └── main/
        └── Test.java          ← Task 5 (@Component, @Autowired, main)
```

## Spring Concepts được áp dụng

| Concept | Áp dụng tại |
|---|---|
| IoC Container | `AnnotationConfigApplicationContext` trong `main()` |
| Component Scan | `@ComponentScan("com.lms")` trong `AppConfig` |
| Bean (Service) | `@Service` trên `LmsDataService` |
| Bean (Component) | `@Component` trên `Test` |
| Dependency Injection | `@Autowired` inject `LmsDataService` vào `Test` |

## OOP Concepts được áp dụng

| Concept | Áp dụng tại |
|---|---|
| Inheritance | `Instructor extends LmsUser` |
| Polymorphism | Gọi `user.printInfo()` trên `List<LmsUser>` → tự dispatch đúng method |
| Serialization | `LmsUser implements Serializable`, `Student implements Serializable` |
| Method Overriding | `Instructor.printInfo()` override `LmsUser.printInfo()` |

## Cách build và chạy

### Yêu cầu
- JDK 1.8+
- Maven 3+

### Build
```bash
cd lms-spring-asm5
mvn clean package
```

### Chạy
```bash
java -jar target/lms-spring-asm5-1.0-SNAPSHOT.jar
```

Hoặc qua Maven:
```bash
mvn exec:java -Dexec.mainClass="com.lms.main.Test"
```

## Expected Output

```
============================================================
         LMS - User Management System
============================================================

--- [1] Saving users to users.dat ---
>>> Đã lưu 4 user(s) vào file: users.dat

--- [2] Saving students to students.dat ---
>>> Đã lưu 4 student(s) vào file: students.dat

--- [3] Loading & displaying users (Polymorphism demo) ---
>>> Đã đọc 4 user(s) từ file: users.dat
User [ID=1, Name=Alice Nguyen, Email=alice@fpt.com]
Instructor [ID=2, Name=John Doe, Email=john.doe@fpt.com, Dept=Engineering, Bio=Spring Framework Expert]
Instructor [ID=3, Name=Jane Smith, Email=jane.smith@fpt.com, Dept=Data Science, Bio=Machine Learning Researcher]
User [ID=4, Name=Bob Tran, Email=bob.tran@fpt.com]

--- [4] Loading & displaying students ---
>>> Đã đọc 4 student(s) từ file: students.dat
Student [ID=101, Name=Jane Smith, GPA=3.75]
Student [ID=102, Name=Minh Le, GPA=3.5]
Student [ID=103, Name=Huong Pham, GPA=3.9]
Student [ID=104, Name=David Tran, GPA=2.8]

============================================================
   Done!
============================================================
```

## Giải thích Polymorphism (Bước 3)

Khi đọc từ file, list có kiểu `List<LmsUser>` nhưng chứa cả `LmsUser` và `Instructor`.
Khi gọi `user.printInfo()` trong vòng lặp:
- Nếu object thực sự là `LmsUser` → chạy `LmsUser.printInfo()` → in "User [...]"
- Nếu object thực sự là `Instructor` → chạy `Instructor.printInfo()` → in "Instructor [...]"

Java tự động dispatch đúng method dựa trên **kiểu thực tế** của object (dynamic dispatch).
