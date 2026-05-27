# Quiz Management System

## Công nghệ
- Java 17, JDBC, H2 Embedded Database, JUnit 5, Maven

## Cấu trúc project
```
src/
├── main/
│   ├── java/
│   │   ├── Main.java            # Console menu
│   │   ├── model/Quiz.java      # Entity
│   │   ├── database/DBConnection.java
│   │   ├── dao/QuizDAO.java     # CRUD via CallableStatement
│   │   └── validation/Validator.java
│   └── resources/
│       └── schema.sql           # H2 table + 5 stored procedures
└── test/java/dao/
    └── QuizDAOTest.java         # 6 JUnit 5 test cases
```

## Chạy chương trình

```bash
# Compile và chạy
mvn compile exec:java -Dexec.mainClass="Main"

# Hoặc dùng IDE: Run Main.java
```

> Database H2 tự động tạo tại `./data/quiz_db` khi chạy lần đầu.

## Chạy Unit Test

```bash
mvn test
```

## Entity: Quiz

| Field | Type | Validation |
|-------|------|------------|
| id | VARCHAR(10) | Không trùng, 2–10 ký tự alphanumeric |
| question | VARCHAR(255) | Không rỗng |
| answer | VARCHAR(255) | Không rỗng |
| category | VARCHAR(100) | Không rỗng |
| difficulty | INT | 1–5 |
| createdBy | VARCHAR(100) | Không rỗng |

## Stored Procedures (H2 ALIAS)

| Procedure | Mô tả |
|-----------|-------|
| `insert_quiz` | Thêm quiz mới |
| `get_all_quizzes` | Lấy toàn bộ quizzes |
| `update_quiz` | Cập nhật quiz theo ID |
| `delete_quiz` | Xóa quiz theo ID |
| `find_quiz_by_id` | Tìm quiz theo ID |
